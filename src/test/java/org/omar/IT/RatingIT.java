package org.omar.IT;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.omar.recipemanagementsystem.RecipesApplication;
import org.omar.recipemanagementsystem.review.entity.ReviewRequest;
import org.omar.recipemanagementsystem.review.entity.Review;
import org.omar.recipemanagementsystem.users.boundary.RegistrationRequest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RecipesApplication.class)
class RatingIT {

  record IdResponse(String id) {
  }

  @LocalServerPort
  private int port;

  private final String ratingResource = "/recipes/api/rating/";
  private final String host = "http://localhost:";
  private final RestTemplate restTemplate = new RestTemplate();
  private final HttpHeaders authHeaders = basicAuthHeaders("user.kenji@email.com", "password123");
  private final HttpHeaders secondUserAuthHeaders = basicAuthHeaders("review.tester@email.com", "12345678");

  private long ratingId;

  private static HttpHeaders basicAuthHeaders(String username, String password) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBasicAuth(username, password);
    return headers;
  }

  @BeforeAll
  void createUser() {
    ResponseEntity<IdResponse> response = restTemplate.postForEntity(host + port + "/recipes/api/user",
        new RegistrationRequest("review.tester@email.com", "12345678", "ROLE_USER"), IdResponse.class);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  @Order(1)
  void postAndGetRating() {
    ReviewRequest ratingRequest = new ReviewRequest(1L, 9, "Simple Review");

    ResponseEntity<IdResponse> createResponse = restTemplate.exchange(host + port + ratingResource,
        HttpMethod.POST, new HttpEntity<>(ratingRequest, authHeaders), IdResponse.class);
    Assertions.assertEquals(HttpStatus.OK, createResponse.getStatusCode());
    ratingId = Long.parseLong(createResponse.getBody().id());

    ResponseEntity<Review> response = restTemplate.exchange(host + port + ratingResource + "/" + ratingId,
        HttpMethod.GET, new HttpEntity<>(authHeaders), Review.class);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals("Simple Review", response.getBody().getReview());
    Assertions.assertEquals(9, response.getBody().getRating());
  }

  @Test
  @Order(2)
  void getRatingsBasedOnRecipe() {
    ReviewRequest ratingRequest = new ReviewRequest(1L, 7, "Second Review");
    ResponseEntity<IdResponse> createResponse = restTemplate.exchange(host + port + ratingResource,
        HttpMethod.POST, new HttpEntity<>(ratingRequest, secondUserAuthHeaders), IdResponse.class);
    Assertions.assertEquals(HttpStatus.OK, createResponse.getStatusCode());
    long secondRatingId = Long.parseLong(createResponse.getBody().id());

    String uri = UriComponentsBuilder.fromUriString(host + port + ratingResource)
        .queryParam("recipeId", 1)
        .build()
        .toUriString();
    ResponseEntity<List<Review>> response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(authHeaders),
        new ParameterizedTypeReference<List<Review>>() {
        });
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    List<Review> reviews = response.getBody();
    Optional<Review> firstRating = reviews.stream()
        .filter(rating -> rating.getId().equals(ratingId))
        .findFirst();
    Optional<Review> secondRating = reviews.stream()
        .filter(rating -> rating.getId().equals(secondRatingId))
        .findFirst();
    Assertions.assertTrue(firstRating.isPresent());
    Assertions.assertTrue(secondRating.isPresent());
    Assertions.assertEquals(9, firstRating.get().getRating());
    Assertions.assertEquals(7, secondRating.get().getRating());
  }

  @Test
  @Order(3)
  void putRating() {
    ReviewRequest ratingRequest = new ReviewRequest(1L, 8, "Edited review");

    ResponseEntity<Object> putResponse = restTemplate.exchange(host + port + ratingResource + "/" + ratingId,
        HttpMethod.PUT, new HttpEntity<>(ratingRequest, authHeaders), Object.class);
    Assertions.assertEquals(HttpStatus.OK, putResponse.getStatusCode());

    ResponseEntity<Review> response = restTemplate.exchange(host + port + ratingResource + "/" + ratingId,
        HttpMethod.GET, new HttpEntity<>(authHeaders), Review.class);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals("Edited review", response.getBody().getReview());
    Assertions.assertEquals(8, response.getBody().getRating());
  }

  @Test
  @Order(4)
  void deleteRating() {
    ResponseEntity<Void> deleteResponse = restTemplate.exchange(host + port + ratingResource + "/" + ratingId,
        HttpMethod.DELETE, new HttpEntity<>(authHeaders), Void.class);
    Assertions.assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

    try {
      restTemplate.exchange(host + port + ratingResource + "/" + ratingId, HttpMethod.GET,
          new HttpEntity<>(authHeaders), Review.class);
    } catch (HttpClientErrorException.NotFound e) {
      Assertions.assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
      return;
    }
    Assertions.fail("Expected a not found response");
  }
}
