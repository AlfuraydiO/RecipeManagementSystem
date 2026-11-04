package org.omar.IT;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.omar.recipemanagementsystem.RecipesApplication;
import org.omar.recipemanagementsystem.recipe.entity.Tag;
import org.omar.recipemanagementsystem.recipe.entity.bodies.tag.TagResponse;
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
class TagIT {

  @LocalServerPort
  private int port;

  private final String tagResource = "/recipes/api/tag";
  private final String host = "http://localhost:";
  private final RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

  private final HttpHeaders chefHeaders = basicAuthHeaders("chef.omar@email.com", "password123");
  private long tagId;

  private static HttpHeaders basicAuthHeaders(String username, String password) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBasicAuth(username, password);
    return headers;
  }

  @BeforeAll
  void createUser() {
    ResponseEntity<Void> response = restTemplate.postForEntity(host + port + "/recipes/api/user",
        new RegistrationRequest("tag.tester@email.com", "12345678", "ROLE_USER"), Void.class);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  @Order(1)
  void postAndGetTag() {
    Tag tag = new Tag(null, "NewTag", "TestTag", "NEWType");

    ResponseEntity<TagResponse> createResponse = restTemplate.exchange(host + port + tagResource,
        HttpMethod.POST, new HttpEntity<>(tag, chefHeaders), TagResponse.class);
    Assertions.assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
    tagId = Long.parseLong(createResponse.getBody().id());

    ResponseEntity<TagResponse> response = restTemplate.exchange(host + port + tagResource + "/" + tagId,
        HttpMethod.GET, new HttpEntity<>(chefHeaders), TagResponse.class);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals("NewTag", response.getBody().name());
    Assertions.assertEquals("TestTag", response.getBody().description());
  }

  @Test
  @Order(2)
  void patchTag() {
    String body = """
                {"type":"Patched Tag type",
        "description":"this tag has been patched"}
            """;

    chefHeaders.add("Content-Type", "application/merge-patch+json");
    ResponseEntity<TagResponse> patchedResponse = restTemplate.exchange(host + port + tagResource + "/" + tagId,
        HttpMethod.PATCH, new HttpEntity<>(body, chefHeaders), TagResponse.class);
    Assertions.assertEquals(HttpStatus.OK, patchedResponse.getStatusCode());

    ResponseEntity<TagResponse> response = restTemplate.exchange(host + port + tagResource + "/" + tagId,
        HttpMethod.GET, new HttpEntity<>(chefHeaders), TagResponse.class);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals("Patched Tag type", response.getBody().type());
    Assertions.assertEquals("this tag has been patched", response.getBody().description());
  }

  @Test
  @Order(3)
  void deleteTag() {
    ResponseEntity<Void> deleteResponse = restTemplate.exchange(host + port + tagResource + "/" + tagId,
        HttpMethod.DELETE, new HttpEntity<>(chefHeaders), Void.class);
    Assertions.assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

    try {
      restTemplate.exchange(host + port + tagResource + "/" + tagId, HttpMethod.GET,
          new HttpEntity<>(chefHeaders), TagResponse.class);
    } catch (HttpClientErrorException.NotFound e) {
      Assertions.assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
      return;
    }
    Assertions.fail("Expected a not found response");
  }
}
