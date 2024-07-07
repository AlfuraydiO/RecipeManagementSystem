package org.omar.recipes.rating.controller;

import jakarta.transaction.Transactional;
import org.omar.recipes.users.controller.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import org.omar.recipes.rating.boundary.RatingRequest;
import org.omar.recipes.rating.entity.Rating;
import org.omar.recipes.recipe.controller.RecipeService;
import org.omar.recipes.recipe.entity.Recipe;
import org.omar.recipes.users.entity.UserAccount;
import org.springframework.context.ApplicationEventPublisher;

@Service
@Transactional
public class RatingService {

    private final ApplicationEventPublisher events;

    RatingRepository ratingRepository;
    UserAccountService accountService;
    RecipeService recipeService;

    public RatingService(ApplicationEventPublisher events, RatingRepository ratingRepository, UserAccountService accountService) {
        this.events = events;
        this.ratingRepository = ratingRepository;
        this.accountService = accountService;
    }

    public Optional<Rating> getRatingById(long id) {
        return ratingRepository.findById(id);
    }

    public Optional<Rating> saveRecipe(RatingRequest ratingrequest, String email) {
        Rating rating=new Rating();
        rating.setLocalDateTime(LocalDateTime.now());
        rating.setUserAccount(accountService.loadUserByEmail(email));
        rating.setRecipeRating(ratingrequest.rating());
        rating.setReview(ratingrequest.review());
        if(!recipeService.existsById(ratingrequest.recipeId())){
         return Optional.empty();
        }
        rating.setRecipe(new Recipe(ratingrequest.recipeId()));
        Optional<Rating> optionalRating = Optional.of(ratingRepository.save(rating));
        events.publishEvent(new RecipeRatingEvent(optionalRating.get()));
        return optionalRating;
    }

    public ResponseEntity<?> updateRating(Long id, RatingRequest Ratingrequest, String email) {
        Optional<Rating> exists = ratingRepository.findById(id);
        if (exists.isPresent()) {
            UserAccount user = accountService.loadUserByEmail(email);
            if (email.equals(exists.get().getUserAccount().getEmail())) {
                Rating rating = exists.get();
                rating.setLocalDateTime(LocalDateTime.now());
                rating.setId(exists.get().getId());
                rating.setUserAccount(user);
                rating.setReview(Ratingrequest.review());
                rating.setRecipeRating(Ratingrequest.rating());
                Rating saved = ratingRepository.save(rating);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public HttpStatus removeRecipe(Long id, String email) {
        Optional<Rating> byId = ratingRepository.findById(id);
        if (byId.isPresent()) {
            if (byId.get().getUserAccount().getEmail().equals(email)) {
                ratingRepository.delete(byId.get());
                return HttpStatus.NO_CONTENT;
            } else {
                return HttpStatus.FORBIDDEN;
            }
        } else {
            return HttpStatus.NOT_FOUND;
        }

    }
}
