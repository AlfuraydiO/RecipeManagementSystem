package org.omar;


import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.omar.recipes.RecipesApplication;
import org.omar.recipes.recipe.entity.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context. TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RecipesApplication.class)
public class recipesIT {


    @LocalServerPort
    private int port;

    private  String RecipeResource="/recipes/api/recipe/";

    private TestRestTemplate restTemplate = new TestRestTemplate("omar@email.com","21423333");

    @Test
    void getRecipeTest() throws Exception {

        ResponseEntity<Recipe> response = this.restTemplate.getForEntity("http://localhost:" + port + "/recipes/api/recipe/1",Recipe.class);
        System.out.println(response.getBody().toString());
       // String jsonString = response.getBody();
        ObjectMapper mapper = new ObjectMapper();
       // Recipe recipe = mapper.readValue(jsonString, Recipe.class);
        //Assertions.assertEquals(recipe.getName(),"Cheesy Tomato Pasta");
    }

}
