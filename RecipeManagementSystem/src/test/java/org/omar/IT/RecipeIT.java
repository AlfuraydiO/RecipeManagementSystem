package org.omar.IT;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.*;
import org.omar.recipes.RecipesApplication;
import org.omar.recipes.recipe.boundary.requestAndResponseBodies.RecipeRequest;
import org.omar.recipes.recipe.entity.Recipe;
import org.omar.recipes.users.boundary.RegistrationRequest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RecipesApplication.class)
public class RecipeIT {

    record idResponse(String id){};

    @LocalServerPort
    private int port;

    private String RecipeResource="/recipes/api/recipe/";
    private String host="http://localhost:";
    private TestRestTemplate restTemplate = new TestRestTemplate();

    private long recipeId;

    @BeforeAll
      void CreateUser(){
        ResponseEntity response = this.restTemplate.postForEntity( host+port + "/recipes/api/register"
                ,new RegistrationRequest("tester@email.com","12345678","ROLE_CHEF"),idResponse.class);
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
        restTemplate = new TestRestTemplate("tester@email.com","12345678");

    }

    @Test
    @Order(1)
    void getRecipeTest() throws Exception {
        ResponseEntity<Recipe> response = this.restTemplate.getForEntity(host + port + RecipeResource + "1",Recipe.class);
         Recipe recipe = response.getBody();
        Assertions.assertEquals("Cheesy Tomato Pasta",recipe.getName());
    }
    
    @Test
    void UnauthorizedPutRecipeTest() {
        RecipeRequest recipeRequest = new RecipeRequest("Steak","Simple desription", "Lunch,Dinner",List.of("ingredients 1 ","ingredients 2"),List.of("description 1","description 2"),new int[]{1,2,3});
        ResponseEntity<Object> putResponse =  ResponseEntity.ofNullable(Object.class);
        try {
            putResponse = this.restTemplate.withBasicAuth("tester@email.com","12345678").exchange(host + port + RecipeResource + 1, HttpMethod.PUT, new HttpEntity<>(recipeRequest), Object.class);
        }catch (ResourceAccessException e){
            System.out.println(e.getCause());
        }
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED,putResponse.getStatusCode());

    }

    @Test
    void UnauthorizedDeleteRecipeTest() {
        //RecipeRequest recipeRequest = new RecipeRequest("Steak","Simple desription", List.of("ingredients 1 ","ingredients 2"),List.of("description 1","description 2"),new int[]{1,2,3});
        ResponseEntity<Object> putResponse =  ResponseEntity.ofNullable(Object.class);
        putResponse = this.restTemplate.withBasicAuth("tester@email.com","12345678").exchange(host + port + RecipeResource + 1, HttpMethod.DELETE, new HttpEntity<>(null), Object.class);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED,putResponse.getStatusCode());
    }

    @Test
    @Order(2)
    void postRecipeTest() throws Exception {
        RecipeRequest recipeRequest = new RecipeRequest("Test Steak","Simple desription","Lunch,Dinner", List.of("ingredients 1 ","ingredients 2"),List.of("description 1","description 2"),new int[]{1,2});

        ResponseEntity<idResponse> idresponse = this.restTemplate.postForEntity(host + port + RecipeResource + "/new",recipeRequest,idResponse.class);
        Assertions.assertEquals(idresponse.getStatusCode(), HttpStatus.OK);
        recipeId= Long.parseLong(idresponse.getBody().id);
        ResponseEntity<Recipe> response = this.restTemplate.getForEntity(host + port + RecipeResource + idresponse.getBody().id,Recipe.class);
        Recipe recipe = response.getBody();
        Assertions.assertEquals("Test Steak",recipe.getName());
        Assertions.assertEquals(2,recipe.getTags().size());
    }

    @Test
    @Order(3)
    void putRecipeTest() throws Exception {
        RecipeRequest recipeRequest = new RecipeRequest("Steak","Simple desription","Lunch,Dinner", List.of("ingredients 1 ","ingredients 2"),List.of("description 1","description 2"),new int[]{1,2,3});
        //
        ResponseEntity<Object> putResponse = this.restTemplate.exchange(host + port + RecipeResource + recipeId, HttpMethod.PUT, new HttpEntity<>(recipeRequest), Object.class);
        Assertions.assertEquals(HttpStatus.NO_CONTENT,putResponse.getStatusCode());
        ResponseEntity<Recipe> response = this.restTemplate.getForEntity(host + port + RecipeResource + recipeId,Recipe.class);
        Recipe recipe = response.getBody();

        Assertions.assertEquals("Steak",recipe.getName());
        Assertions.assertEquals(3,recipe.getTags().size());
    }

    @Test
    @Order(4)
    void DeleteRecipeTest() throws Exception {
        this.restTemplate.delete(host + port + RecipeResource +recipeId);
        ResponseEntity<Recipe> response = this.restTemplate.getForEntity(host + port + RecipeResource + recipeId,Recipe.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }



}
