package org.omar.recipes.recipe.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.omar.recipes.rating.controller.RatingService;
import org.omar.recipes.rating.entity.Rating;
import org.omar.recipes.recipe.boundary.RecipeRequest;
import org.omar.recipes.recipe.entity.Recipe;
import org.omar.recipes.recipe.entity.Tag;
import org.omar.recipes.users.controller.UserAccountService;
import org.omar.recipes.users.entity.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class RecipeService {

    private RecipeRepository recipeRepository;
    private UserAccountService userService;
    private TagService tagService;
    private Validator validator;
    private RatingService ratingService;

    public RecipeService(RecipeRepository recipeRepository, UserAccountService userService, TagService tagService, Validator validator) {
        this.recipeRepository = recipeRepository;
        this.userService = userService;
        this.tagService = tagService;
        this.validator = validator;
    }


    public Optional<Recipe> getRecipeById(long id) {
        return recipeRepository.findById(id);
    }

    public boolean existsById(long id) {
        return recipeRepository.existsById(id);
    }

    public Recipe saveRecipe(RecipeRequest request, String email) {
        Recipe recipe = new Recipe(null, request.name(), request.description(), request.ingredients(),
                request.directions());
        recipe.setDate(LocalDateTime.now());
        recipe.setUser(email == null ? null : userService.loadUserByEmail(email));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        if (!violations.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, violations.stream().map(ConstraintViolation::getMessage).toList().toString());
        }
        List<Tag> tagList = new ArrayList<>();
        if(request.tags()!=null){
        for (int id : request.tags()) {
            Tag tag = tagService.getTagById(id);
            tagList.add(tag);
        }}
        recipe.setTags(new HashSet<>(tagList));
        return recipeRepository.save(recipe);
    }

    public ResponseEntity<?> updateRecipe(Long id, RecipeRequest request, String email) {
        Optional<Recipe> exists = recipeRepository.findById(id);
        if (exists.isPresent()) {
            UserAccount chefUser = userService.loadUserByEmail(email);
            if (exists.get().getUser().getEmail().equals(email)) {
                Recipe recipe = new Recipe(exists.get().getId(), request.name(), request.description(), request.ingredients(),
                        request.directions());
                recipe.setDate(LocalDateTime.now());
                recipe.setUser(chefUser);
                Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
                if (!violations.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, violations.stream().map(ConstraintViolation::getMessage).toList().toString());
                }
                List<Tag> tagList = new ArrayList<>();
                for (int tagid : request.tags()) {
                    Tag tag = tagService.getTagById(tagid);
                    tagList.add(tag);
                }
                recipe.setTags(new HashSet<>(tagList));
                Recipe saved = recipeRepository.save(recipe);
                return ResponseEntity.noContent().build();
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are Unauthorized to update this recipe id " + id);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe " + id + " Not Found !");
        }
    }

    public List<Recipe> SearchRecipe(String name) {
        if (!name.isEmpty()) {
            return recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name);
        }
        return new ArrayList<Recipe>();
    }

    public HttpStatus removeRecipe(Long id, String email) {
        Optional<Recipe> byId = recipeRepository.findById(id);
        if (byId.isPresent()) {
            if (byId.get().getUser().getEmail().equals(email)) {
                recipeRepository.delete(byId.get());
                return HttpStatus.NO_CONTENT;
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are Unauthorized to delete this recipe id " + id);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe " + id + " Not Found !");
        }

    }

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedDelay = 10l)
    public void UpdateRecipeRating() {
        Iterable<Recipe> allRecipes = this.getAllRecipes();
        Iterator<Recipe> iterator = allRecipes.iterator();
        while (iterator.hasNext()) {
            Recipe next = iterator.next();
            long CountRatingByRecipe = ratingService.CountRatingByRecipe(next.getId());
            if (CountRatingByRecipe > 0) {
                long totalRatingscore = 0;
                List<Rating> ratingById = ratingService.getRatingsByRecipe(next.getId());
                for (Rating rating : ratingById) {
                    totalRatingscore += rating.getRecipeRating();
                }
                next.setTotalRating(totalRatingscore / CountRatingByRecipe);
                try {
                    recipeRepository.save(next);
                } catch (Exception e) {
                    System.err.println("issue");
                }

            }

        }
    }

    public Iterable<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public RatingService getRatingService() {
        return ratingService;
    }

    @Autowired
    public void setRatingService(@Lazy RatingService ratingService) {
        this.ratingService = ratingService;
    }


}
