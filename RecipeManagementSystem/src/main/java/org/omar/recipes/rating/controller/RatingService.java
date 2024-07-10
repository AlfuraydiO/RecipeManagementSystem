package org.omar.recipes.rating.controller;

import jakarta.transaction.Transactional;
import org.omar.recipes.users.controller.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.omar.recipes.rating.boundary.RatingRequest;
import org.omar.recipes.rating.entity.Rating;
import org.omar.recipes.recipe.controller.RecipeService;
import org.omar.recipes.recipe.entity.Recipe;
import org.omar.recipes.users.entity.UserAccount;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class RatingService {

    RatingRepository ratingRepository;
    UserAccountService accountService;
    RecipeService recipeService;

    public RatingService(RatingRepository ratingRepository, UserAccountService accountService, RecipeService recipeService) {
        this.ratingRepository = ratingRepository;
        this.accountService = accountService;
        this.recipeService = recipeService;
    }

    public Rating getRatingById(long id) {
        Optional<Rating> rating = ratingRepository.findById(id);
        Rating ratingByid = rating.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rating with " + id + " Not Found !"));
        return ratingByid;
    }

    public Optional<Rating> saveRating(RatingRequest ratingrequest, String email) {
        Rating rating = new Rating();
        rating.setLocalDateTime(LocalDateTime.now());
        rating.setUserAccount(accountService.loadUserByEmail(email));
        rating.setRecipeRating(ratingrequest.rating());
        rating.setReview(ratingrequest.review());
        if (!recipeService.existsById(ratingrequest.recipeId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe " + ratingrequest.recipeId() + " Not Found !");
        }
        rating.setRecipe(new Recipe(ratingrequest.recipeId()));
        Optional<Rating> optionalRating = Optional.of(ratingRepository.save(rating));

        //events.publishEvent(new RecipeRatingEvent(optionalRating.get()));
        return optionalRating;
    }

    public Rating updateRating(Long id, RatingRequest Ratingrequest, String email) {
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
                return saved;
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are Unauthorized to update this rating id " + id);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "rating with id" + id + " Not Found !");
        }
    }

    public boolean removeRating(Long id, String email) {
        Optional<Rating> byId = ratingRepository.findById(id);
        if (byId.isPresent()) {
            if (byId.get().getUserAccount().getEmail().equals(email)) {
                ratingRepository.delete(byId.get());
                return true;
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are Unauthorized to delete this rating, id " + id);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "rating with id" + id + " Not Found !");
        }

    }

    public List<Rating> getRatingsByRecipe(long id) {
        if (!recipeService.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe with id" + id + " Not Found !");
        }
        List<Rating> ratings = new ArrayList<>();
        ratings=ratingRepository.findRatingByRecipe(id);
        return ratings;
    }
    
    public long CountRatingByRecipe(long id) {
        if (!recipeService.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe with id" + id + " Not Found !");
        }
        return ratingRepository.countRatingByRecipe(id);
    }
}
