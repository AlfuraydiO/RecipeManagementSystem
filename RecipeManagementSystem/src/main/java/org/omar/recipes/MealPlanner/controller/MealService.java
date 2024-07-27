package org.omar.recipes.MealPlanner.controller;

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
import java.util.Optional;
import java.util.Set;

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

    public ResponseEntity removeMeal(String email, long id){
         //UserAccount userAccount = userAccountService.loadUserByEmail(email);
        Optional<MealPlan> mealPlamByid = mealPlanRepository.findById(id);
        if (mealPlamByid.isPresent()){
            if(mealPlamByid.get().getMasterMealPlan().getUserAccount().getEmail().equals(email)){
                try {
                    mealPlanRepository.delete(mealPlamByid.get());
                    return ResponseEntity.noContent().build();
                }catch (Exception e){
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getLocalizedMessage());
                }

            }else{
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"You are not authorized to delete this meal plan "+id);
            }
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No meal Plan percent with this id "+id);
        }
    }

    public void editMeal(){

    }

    public MealPlan getMeal(String email,long id){
       // UserAccount userAccount = userAccountService.loadUserByEmail(email);
        Optional<MealPlan> mealPlamByid = mealPlanRepository.findById(id);
        if (mealPlamByid.isPresent()){
            if(mealPlamByid.get().getMasterMealPlan().getUserAccount().getEmail().equals(email)){
                return  mealPlamByid.get();
            }else{
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"You are not authorized to view this meal plan "+id);
            }
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No meal Plan percent with this id "+id);
        }

    }

    //Fix
    public ResponseEntity<Object> editMealPlan(String email, long id, MealPlan mealPlan) {
        UserAccount userAccount = userAccountService.loadUserByEmail(email);
        Optional<MealPlan> mealPlamByid = mealPlanRepository.findById(id);
        if (mealPlamByid.isPresent()){
            MealPlan plan = mealPlamByid.get();
            Optional<MasterMealPlan> optionalMasterMealPlan = masterMealPlanRepository.findById(mealPlan.getMasterMealPlanId());

            if(optionalMasterMealPlan.isPresent()){
                if(optionalMasterMealPlan.get().getUserAccount().equals(userAccount)){
                    mealPlan.setMasterMealPlan(optionalMasterMealPlan.get());
                }else{
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Incorrect master plan id"+mealPlan.getMasterMealPlanId());
                }
            }else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No meal Plan percent with this master plan id "+id);
            }
            if(plan.getMasterMealPlan().getUserAccount().getEmail().equals(email)){
                try {
                    plan.setMasterMealPlan(optionalMasterMealPlan.get());
                    //here?
                    for(Meal meal:plan.getMeals()){
                        Set<Recipe> recipes = new HashSet<>();
                        for(Recipe recipe:meal.getRecipes().stream().toList()){
                            Optional<Recipe> optionalRecipe = this.recipeRepository.findById(recipe.getId());
                            if(optionalRecipe.isPresent()){
                                recipes.add(optionalRecipe.get());
                            }else{
                                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Recipe not found");
                            }
                        }
                        meal.setRecipes(recipes);
                    }

                    mealPlanRepository.save(plan);
                    return ResponseEntity.noContent().build();
                }catch (Exception e){
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getLocalizedMessage());
                }
            }else{
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"You are not authorized to edit this meal plan "+id);
            }
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No meal Plan percent with this id "+id);
        }
    }
}
