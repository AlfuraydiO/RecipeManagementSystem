package org.omar.recipes.MealPlanner.controller;

import jakarta.transaction.Transactional;
import org.omar.recipes.MealPlanner.entity.MasterMealPlan;
import org.omar.recipes.MealPlanner.entity.Meal;
import org.omar.recipes.MealPlanner.entity.MealPlan;
import org.omar.recipes.recipe.controller.RecipeRepository;
import org.omar.recipes.recipe.entity.Recipe;
import org.omar.recipes.users.controller.UserAccountService;
import org.omar.recipes.users.entity.UserAccount;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.omar.recipes.MealPlanner.controller.MealPlanSpecs.*;

@Service
public class MealService {

    MealPlanRepository mealPlanRepository;
    UserAccountService userAccountService;
    MasterMealPlanRepository masterMealPlanRepository;
    RecipeRepository recipeRepository;

    public MealService(MealPlanRepository mealPlanRepository, UserAccountService userAccountService, MasterMealPlanRepository masterMealPlanRepository, RecipeRepository recipeRepository) {
        this.mealPlanRepository = mealPlanRepository;
        this.userAccountService = userAccountService;
        this.masterMealPlanRepository = masterMealPlanRepository;
        this.recipeRepository = recipeRepository;
    }

    @Transactional
    public ResponseEntity removeMeal(String email, long id) {
        //UserAccount userAccount = userAccountService.loadUserByEmail(email);
        Optional<MealPlan> mealPlamByid = mealPlanRepository.findById(id);
        if (mealPlamByid.isPresent()) {
            MealPlan mealPlan = mealPlamByid.get();
            if (mealPlan.getMasterMealPlan().getUserAccount().getEmail().equals(email)) {
                try {
                    //Handel error
                    MasterMealPlan masterMealPlan = this.masterMealPlanRepository.findById(mealPlan.getMasterMealPlanId()).get();
                    masterMealPlan.getMealPlans().remove(mealPlan);
                    masterMealPlanRepository.save(masterMealPlan);
                    mealPlanRepository.delete(mealPlan);

                    return ResponseEntity.noContent().build();
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
                }

            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to delete this meal plan " + id);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No meal Plan percent with this id " + id);
        }
    }

    public MealPlan getMeal(String email, long id) {
        // UserAccount userAccount = userAccountService.loadUserByEmail(email);
        Optional<MealPlan> mealPlamByid = mealPlanRepository.findById(id);
        if (mealPlamByid.isPresent()) {
            if (mealPlamByid.get().getMasterMealPlan().getUserAccount().getEmail().equals(email)) {
                return mealPlamByid.get();
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to view this meal plan " + id);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No meal Plan percent with this id " + id);
        }

    }

    //Fix
    @Transactional
    public ResponseEntity<Object> editMealPlan(String email, long id, MealPlan mealPlan) {
        UserAccount userAccount = userAccountService.loadUserByEmail(email);
        Optional<MealPlan> mealPlamByid = mealPlanRepository.findById(id);
        if (mealPlamByid.isPresent()) {
            MealPlan planByid = mealPlamByid.get();
            Optional<MasterMealPlan> optionalMasterMealPlan = masterMealPlanRepository.findById(mealPlan.getMasterMealPlanId());

            if (optionalMasterMealPlan.isPresent()) {
                if (!optionalMasterMealPlan.get().getUserAccount().equals(userAccount)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect master plan id" + mealPlan.getMasterMealPlanId());
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No meal Plan percent with this master plan id " + id);
            }
            if (planByid.getMasterMealPlan().getUserAccount().getEmail().equals(email)) {
                try {
                    planByid.setMasterMealPlan(optionalMasterMealPlan.get());
                    planByid.getMeals().clear();
                    //here?
                    for (Meal meal : mealPlan.getMeals()) {
                        Set<Recipe> recipes = new HashSet<>();
                        for (Recipe recipe : meal.getRecipes().stream().toList()) {
                            Optional<Recipe> optionalRecipe = this.recipeRepository.findById(recipe.getId());
                            if (optionalRecipe.isPresent()) {
                                recipes.add(optionalRecipe.get());
                            } else {
                                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found");
                            }
                        }
                        meal.setRecipes(recipes);
                        planByid.addMeals(meal);
                    }

                    mealPlanRepository.save(planByid);
                    return ResponseEntity.noContent().build();
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
                }
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to edit this meal plan " + id);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No meal Plan percent with this id " + id);
        }
    }

    public List<MealPlan> getAheadeal(String email,int days) {
        // UserAccount userAccount = userAccountService.loadUserByEmail(email);
        List<MealPlan> mealPlamByspec = mealPlanRepository.findAll(DaysAhead(days));
        if (!mealPlamByspec.isEmpty()) {
            return mealPlamByspec;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No meal Plan percent with this id ");
        }

    }
}
