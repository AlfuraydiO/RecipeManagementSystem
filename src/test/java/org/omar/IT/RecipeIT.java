package org.omar.IT;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.omar.recipemanagementsystem.RecipesApplication;
import org.omar.recipemanagementsystem.recipe.entity.bodies.receipe.RecipeRequest;
import org.omar.recipemanagementsystem.recipe.entity.bodies.receipe.ResponseRecipe;
import org.omar.recipemanagementsystem.recipe.entity.bodies.tag.TagHref;
import org.omar.recipemanagementsystem.users.boundary.RegistrationRequest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RecipesApplication.class)
class RecipeIT {

  record IdResponse(String id) {
  }

  @LocalServerPort
  private int port;

  private final String recipeResource = "/recipes/api/recipe";
  private final String host = "http://localhost:";
  private final RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
  private final HttpHeaders chefHeaders = basicAuthHeaders("chef.omar@email.com", "password123");

  private long recipeId;

  private static HttpHeaders basicAuthHeaders(String username, String password) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBasicAuth(username, password);
    return headers;
  }

  @BeforeAll
  void createUser() {
    ResponseEntity<IdResponse> response = restTemplate.postForEntity(host + port + "/recipes/api/user",
        new RegistrationRequest("recipe.tester@email.com", "12345678", "ROLE_CHEF"), IdResponse.class);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  @Order(1)
  void getRecipe() {
    ResponseEntity<ResponseRecipe> response = restTemplate.exchange(host + port + recipeResource + "/1",
        HttpMethod.GET, new HttpEntity<>(chefHeaders), ResponseRecipe.class);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals("Cheesy Tomato Pasta", response.getBody().name());
  }

  @Test
  @Order(2)
  void unauthorizedPutRecipe() {
    RecipeRequest recipeRequest = recipeRequest("Steak", List.of(new TagHref(1, "."), new TagHref(2, ".")));

    try {
      restTemplate.exchange(host + port + recipeResource + "/1", HttpMethod.PUT,
          new HttpEntity<>(recipeRequest), Object.class);
    } catch (HttpClientErrorException.Unauthorized e) {
      Assertions.assertEquals(HttpStatus.UNAUTHORIZED, e.getStatusCode());
      return;
    }
    Assertions.fail("Expected an unauthorized response");
  }

  @Test
  @Order(3)
  void unauthorizedDeleteRecipe() {
    try {
      restTemplate.exchange(host + port + recipeResource + "/1", HttpMethod.DELETE,
          new HttpEntity<>((Void) null), Object.class);
    } catch (HttpClientErrorException.Unauthorized e) {
      Assertions.assertEquals(HttpStatus.UNAUTHORIZED, e.getStatusCode());
      return;
    }
    Assertions.fail("Expected an unauthorized response");
  }

  @Test
  @Order(4)
  void postRecipe() {
    RecipeRequest recipeRequest = recipeRequest("Test Steak",
        List.of(new TagHref(1, "."), new TagHref(2, ".")));

    ResponseEntity<ResponseRecipe> createResponse = restTemplate.exchange(host + port + recipeResource,
        HttpMethod.POST, new HttpEntity<>(recipeRequest, chefHeaders), ResponseRecipe.class);
    Assertions.assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
    recipeId = createResponse.getBody().id();

    ResponseEntity<ResponseRecipe> response = restTemplate.exchange(host + port + recipeResource + "/" + recipeId,
        HttpMethod.GET, new HttpEntity<>(chefHeaders), ResponseRecipe.class);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals("Test Steak", response.getBody().name());
    Assertions.assertEquals(2, response.getBody().tags().size());
  }

  @Test
  @Order(5)
  void putRecipe() {
    RecipeRequest recipeRequest = recipeRequest("Steak",
        List.of(new TagHref(1, "."), new TagHref(2, "."), new TagHref(3, ".")));

    ResponseEntity<ResponseRecipe> putResponse = restTemplate.exchange(host + port + recipeResource + "/" + recipeId,
        HttpMethod.PUT, new HttpEntity<>(recipeRequest, chefHeaders), ResponseRecipe.class);
    Assertions.assertEquals(HttpStatus.OK, putResponse.getStatusCode());

    ResponseEntity<ResponseRecipe> response = restTemplate.exchange(host + port + recipeResource + "/" + recipeId,
        HttpMethod.GET, new HttpEntity<>(chefHeaders), ResponseRecipe.class);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals("Steak", response.getBody().name());
    Assertions.assertEquals(3, response.getBody().tags().size());
  }

  @Test
  @Order(6)
  void patchRecipe() {
    String body = """
           {"name":"simpleSteak",
        "category": "dinner"}
              """;
    chefHeaders.add("Content-Type", "application/merge-patch+json");
    ResponseEntity<ResponseRecipe> patchResponse = restTemplate.exchange(host + port + recipeResource + "/" + recipeId,
        HttpMethod.PATCH, new HttpEntity<>(body, chefHeaders), ResponseRecipe.class);
    Assertions.assertEquals(HttpStatus.OK, patchResponse.getStatusCode());

    ResponseEntity<ResponseRecipe> response = restTemplate.exchange(host + port + recipeResource + "/" + recipeId,
        HttpMethod.GET, new HttpEntity<>(chefHeaders), ResponseRecipe.class);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals("simpleSteak", response.getBody().name());
    Assertions.assertEquals("dinner", response.getBody().category());

  }

  @Test
  @Order(7)
  void deleteRecipe() {
    ResponseEntity<Void> deleteResponse = restTemplate.exchange(host + port + recipeResource + "/" + recipeId,
        HttpMethod.DELETE, new HttpEntity<>(chefHeaders), Void.class);
    Assertions.assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

    try {
      restTemplate.exchange(host + port + recipeResource + "/" + recipeId, HttpMethod.GET,
          new HttpEntity<>(chefHeaders), ResponseRecipe.class);
    } catch (HttpClientErrorException.NotFound e) {
      Assertions.assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
      return;
    }
    Assertions.fail("Expected a not found response");
  }

  private RecipeRequest recipeRequest(String name, List<TagHref> tags) {
    return new RecipeRequest(name, "Simple description", "Lunch,Dinner",
        List.of("ingredients 1", "ingredients 2"), List.of("description 1", "description 2"), tags);
  }

}
