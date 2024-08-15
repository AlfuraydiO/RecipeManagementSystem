/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.omar.IT;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.omar.recipes.RecipesApplication;
import org.omar.recipes.recipe.entity.Tag;
import org.omar.recipes.users.boundary.RegistrationRequest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = RecipesApplication.class)
public class TagIT {

    record idResponse(String id) {};

    @LocalServerPort
    private int PORT;

    private final String TAGRESOURCE = "/recipes/api/tag/";
    private static final String HOST = "http://localhost:";
    private TestRestTemplate restTemplate = new TestRestTemplate();
    private static final String USERNAME = "omar@email.com";
    private static final String PASSWORD = "21423333";
    private long tagId;

    @BeforeAll
    void CreateUser() {
        ResponseEntity response = this.restTemplate.postForEntity(HOST + PORT + "/recipes/api/register",
            new RegistrationRequest("tester@email.com", "12345678", "ROLE_CHEF"), idResponse.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        restTemplate = new TestRestTemplate("tester@email.com", "12345678");
    }

    @Test
    @Order(1)
    void PostAndGetRatingTest() throws Exception {
        Tag tag = new Tag(null, "NewTag", "TestTag", "NEWType");

        ResponseEntity<idResponse> responseEntity = this.restTemplate.postForEntity(HOST + PORT + TAGRESOURCE + "/new", tag, idResponse.class);
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        tagId = Long.parseLong(responseEntity.getBody().id());
        ResponseEntity<Tag> response = this.restTemplate.getForEntity(HOST + PORT + TAGRESOURCE + tagId, Tag.class);
        tag = response.getBody();
        Assertions.assertEquals("NewTag", tag.getName());
        Assertions.assertEquals("TestTag", tag.getDescription());
    }
 
 
    @Test
    @Order(2)
    void DeleteRatingTest() throws Exception {
        this.restTemplate.delete(HOST + PORT + TAGRESOURCE + tagId);
        ResponseEntity<Tag> response = this.restTemplate.getForEntity(HOST + PORT + TAGRESOURCE + tagId, Tag.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

}
