package org.omar.recipemanagementsystem.review.boundary;

import java.net.URI;
import java.util.List;

import org.omar.recipemanagementsystem.review.controller.ReviewService;
import org.omar.recipemanagementsystem.review.entity.ReviewRequest;
import org.omar.recipemanagementsystem.review.entity.ReviewResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/rating")
public class ReviewResource {
  @Autowired
  ReviewService reviewService;

  public ReviewResource(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @GetMapping("{id}")
  public ResponseEntity<ReviewResponse> getRating(@PathVariable Long id) {
    var reviewByid = reviewService.getRatingById(id);
    return ResponseEntity.ok(reviewByid);
  }

  @PutMapping("{id}")
  public ResponseEntity<ReviewResponse> updateRating(@AuthenticationPrincipal UserDetails userDetails,
      @PathVariable Long id,
      @RequestBody @Valid ReviewRequest ratingRequest) {
    var updateReview = reviewService.updateRating(id, ratingRequest, userDetails.getUsername());
    return ResponseEntity.ok(updateReview);
  }

  @PostMapping(produces = "application/json", consumes = "application/json")
  public ResponseEntity<ReviewResponse> PostRating(@AuthenticationPrincipal UserDetails userDetails,
      @RequestBody @Valid ReviewRequest ratingRequest) {
    var reviewResponse = reviewService.saveRating(ratingRequest, userDetails.getUsername());
    return ResponseEntity.created(URI.create(reviewResponse.href())).body(reviewResponse);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Object> deleteRating(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
    boolean status = reviewService.removeRating(id, userDetails.getUsername());
    return ResponseEntity.noContent().build();
  }

  @GetMapping("")
  public ResponseEntity<List> getAllUserRating(@AuthenticationPrincipal UserDetails userDetails,
      @RequestParam(required = true, defaultValue = "") long recipeId) {
    var reviews = reviewService.getRatingsByRecipe(recipeId);
    return ResponseEntity.ok(reviews);
  }

}
