package org.omar.recipemanagementsystem.review.controller;

import jakarta.transaction.Transactional;
import org.omar.recipemanagementsystem.review.entity.ReviewRequest;
import org.omar.recipemanagementsystem.review.entity.Review;
import org.omar.recipemanagementsystem.review.entity.ReviewResponse;
import org.omar.recipemanagementsystem.review.entity.ReviewSummary;
import org.omar.recipemanagementsystem.recipe.controller.RecipeService;
import org.omar.recipemanagementsystem.recipe.entity.Recipe;
import org.omar.recipemanagementsystem.users.controller.UserAccountService;
import org.omar.recipemanagementsystem.users.entity.UserAccount;
import org.omar.recipemanagementsystem.users.entity.user.UserAccountHref;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReviewService {

  ReviewRepository reviewRepository;
  UserAccountService accountService;
  RecipeService recipeService;

  public ReviewService(ReviewRepository reviewRepository, UserAccountService accountService,
      RecipeService recipeService) {
    this.reviewRepository = reviewRepository;
    this.accountService = accountService;
    this.recipeService = recipeService;
  }

  public ReviewResponse getRatingById(long id) {
    Optional<Review> rating = reviewRepository.findById(id);
    Review reviewByid = rating
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review with " + id + " Not Found !"));
    return createRatingReponse(reviewByid);
  }

  public ReviewResponse saveRating(ReviewRequest ratingrequest, String email) {

    var useraccount = accountService.loadUserByEmail(email);
    var ratingbyUserandRecipe = reviewRepository.findReviewByRecipeandUser(ratingrequest.recipeId(),
        useraccount.getId());
    if (ratingbyUserandRecipe.isPresent()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has already Reviewed this recipe");
    }
    if (!recipeService.existsById(ratingrequest.recipeId())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Recipe " + ratingrequest.recipeId() + " Not Found !");
    }

    Review review = new Review();
    review.setUserAccount(useraccount);
    review.setLocalDateTime(LocalDateTime.now());
    review.setRating(ratingrequest.rating());
    review.setReview(ratingrequest.review());
    review.setRecipe(new Recipe(ratingrequest.recipeId()));
    Optional<Review> optionalRating = Optional.of(reviewRepository.save(review));

    // events.publishEvent(new RecipeRatingEvent(optionalRating.get()));
    return createRatingReponse(optionalRating.get());
  }

  public ReviewResponse updateRating(Long id, ReviewRequest Ratingrequest, String email) {
    Optional<Review> exists = reviewRepository.findById(id);
    if (exists.isPresent()) {
      UserAccount user = accountService.loadUserByEmail(email);
      if (email.equals(exists.get().getUserAccount().getEmail())) {
        Review review = exists.get();
        review.setLocalDateTime(LocalDateTime.now());
        review.setId(exists.get().getId());
        review.setUserAccount(user);
        review.setReview(Ratingrequest.review());
        review.setRating(Ratingrequest.rating());
        Review saved = reviewRepository.save(review);
        return createRatingReponse(saved);
      } else {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
            "You are Unauthorized to update this review id " + id);
      }
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "review with id" + id + " Not Found !");
    }
  }

  public boolean removeRating(Long id, String email) {
    Optional<Review> byId = reviewRepository.findById(id);
    if (byId.isPresent()) {
      if (byId.get().getUserAccount().getEmail().equals(email)) {
        reviewRepository.delete(byId.get());
        return true;
      } else {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
            "You are Unauthorized to delete this review, id " + id);
      }
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "review with id" + id + " Not Found !");
    }

  }

  public List<Review> getAllRatingsByRecipe(long id) {
    if (!recipeService.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe with id" + id + " Not Found !");
    }
    List<Review> reviews = new ArrayList<>();
    reviews = reviewRepository.findReviewByRecipe(id);
    return reviews;
  }

  public List<ReviewResponse> getRatingsByRecipe(long id) {

    var reviews = getAllRatingsByRecipe(id);
    if (reviews.isEmpty()) {
      return new ArrayList<ReviewResponse>();
    }
    var reviewResponse = reviews.stream().map(this::createRatingReponse).toList();
    return reviewResponse;
  }

  public ReviewSummary getTotalRatingByRecipe(long id) {
    if (!recipeService.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe with id" + id + " Not Found !");
    }

    long count = reviewRepository.countReviewByRecipe(id);
    if (count == 0) {
      return new ReviewSummary(0, 0);
    }
    long sum = reviewRepository.getSumOfRatingsByRecipe(id);

    return new ReviewSummary(count, (float) sum / count);
  }

  public ReviewResponse createRatingReponse(Review review) {
    UserAccountHref reviewedBy = accountService.getUserLink(review.getUserAccount().getId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "User with id " + review.getUserAccount().getId() + " Not Found !"));
    return new ReviewResponse(
        String.valueOf(review.getId()),
        getRatingHref(review.getId()),
        String.valueOf(review.getRating()),
        reviewedBy,
        review.getLocalDateTime().toString(), review.getReview());
  }

  public String getRatingHref(long id) {
    return ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/api/rating/{id}")
        .buildAndExpand(id)
        .getPath();
  }
}
