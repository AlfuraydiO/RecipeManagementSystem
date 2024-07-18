/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package org.omar.IT;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.omar.recipes.RecipesApplication;
import org.omar.recipes.rating.boundary.RatingRequest;
import org.omar.recipes.rating.entity.Rating;
import org.omar.recipes.users.boundary.RegistrationRequest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author oalfuraydi
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = RecipesApplication.class)
public class RatingIT {

    record idResponse(String id) {};

    @LocalServerPort
    private int PORT;

    private final String RATINGRESOURCE = "/recipes/api/rating/";
    private static final String HOST = "http://localhost:";
    private TestRestTemplate restTemplate = new TestRestTemplate();
    private static final String USERNAME = "omar@email.com";
    private static final String PASSWORD = "21423333";
    private long ratingId;

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
        RatingRequest ratingRequest = new RatingRequest(1L, 9, "Simple Review");

        ResponseEntity<idResponse> idresponse = this.restTemplate.postForEntity(HOST + PORT + RATINGRESOURCE + "/new", ratingRequest, idResponse.class);
        Assertions.assertEquals(idresponse.getStatusCode(), HttpStatus.OK);
        ratingId = Long.parseLong(idresponse.getBody().id());
        ResponseEntity<Rating> response = this.restTemplate.getForEntity(HOST + PORT + RATINGRESOURCE + ratingId, Rating.class);
        Rating rating = response.getBody();
        Assertions.assertEquals("Simple Review", rating.getReview());
        Assertions.assertEquals(9, rating.getRecipeRating());
    }

    @Test
    @Order(2)
    void GetRatingBasedOnRecipeTest() throws Exception {
        RatingRequest ratingRequest = new RatingRequest(1L, 7, "Simple Review");
        ResponseEntity<idResponse> idresponse = this.restTemplate.withBasicAuth(USERNAME, PASSWORD).postForEntity(HOST + PORT + RATINGRESOURCE + "/new", ratingRequest, idResponse.class);
        Assertions.assertEquals(HttpStatus.OK, idresponse.getStatusCode());
        long responseId2 = Long.valueOf(idresponse.getBody().id);
        ratingRequest = new RatingRequest(2L, 2, "Simple Review");
        idresponse = this.restTemplate.withBasicAuth(USERNAME, PASSWORD).postForEntity(HOST + PORT + RATINGRESOURCE + "/new", ratingRequest, idResponse.class);
        List<Rating> ratings = new ArrayList<>();
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(HOST + PORT + RATINGRESOURCE).queryParam("recipeId", 1).build();
        ResponseEntity<List<Rating>> response = this.exchangeAsList(builder.toUriString(), new ParameterizedTypeReference<List<Rating>>() {
        });
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        ratings = response.getBody();
        Assertions.assertEquals(2, ratings.size());
        Optional<Rating> rating1 = ratings.stream().filter(e -> e.getId().equals(ratingId)).findFirst();
        Assertions.assertEquals(9, rating1.get().getRecipeRating());
        Optional<Rating> rating2 = ratings.stream().filter(e -> e.getId().equals(responseId2)).findFirst();
        Assertions.assertEquals(7, rating2.get().getRecipeRating());
    }

    @Test
    @Order(3)
    void PutRatingTest() throws Exception {
        RatingRequest ratingRequest = new RatingRequest(1L, 8, "edited review Review");

        ResponseEntity<Object> putResponse = this.restTemplate.exchange(HOST + PORT + RATINGRESOURCE + ratingId, HttpMethod.PUT, new HttpEntity<>(ratingRequest), Object.class);
        Assertions.assertEquals(HttpStatus.OK, putResponse.getStatusCode());

        ResponseEntity<Rating> response = this.restTemplate.getForEntity(HOST + PORT + RATINGRESOURCE + ratingId, Rating.class);
        Rating rating = response.getBody();
        Assertions.assertEquals("edited review Review", rating.getReview());
        Assertions.assertEquals(8, rating.getRecipeRating());
    }

    @Test
    @Order(4)
    void DeleteRatingTest() throws Exception {
        RatingRequest ratingRequest = new RatingRequest(1L, 9, "Simple Review");

        this.restTemplate.delete(HOST + PORT + RATINGRESOURCE + ratingId);

        ResponseEntity<Rating> response = this.restTemplate.getForEntity(HOST + PORT + RATINGRESOURCE + ratingId, Rating.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    public <T> ResponseEntity<List<T>> exchangeAsList(String uri, ParameterizedTypeReference<List<T>> responseType) {
        return this.restTemplate.exchange(uri, HttpMethod.GET, null, responseType);
    }

}
