
package org.omar.recipes.MealPlanner.controller;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.omar.recipes.MealPlanner.boundery.RequestAndResponseBodies.MealPlanRequest;
import org.omar.recipes.MealPlanner.entity.MasterMealPlan;
import org.omar.recipes.MealPlanner.entity.Meal;
import org.omar.recipes.MealPlanner.entity.MealPlan;
import org.omar.recipes.MealPlanner.entity.enums.IntermittentfastingTypes;
import org.omar.recipes.MealPlanner.entity.enums.MealType;
import org.omar.recipes.recipe.controller.RecipeService;
import org.omar.recipes.recipe.entity.Recipe;
import org.omar.recipes.users.controller.UserAccountService;
import org.omar.recipes.users.entity.UserAccount;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author oalfuraydi
 */
@Service
public class MealPlannerService {

    RecipeService recipeService;

    UserAccountService userAccountService;
    MasterMealPlanRepository masterMealPlanRepository;

    public MealPlannerService(RecipeService recipeService, UserAccountService userAccountService, MasterMealPlanRepository masterMealPlanRepository) {
        this.recipeService = recipeService;
        this.userAccountService = userAccountService;
        this.masterMealPlanRepository = masterMealPlanRepository;
    }

    public MasterMealPlan RequestPlan(String userEmail, MealPlanRequest mealPlanRequest) {
        System.err.println(mealPlanRequest.intermittentfastingType());
        IntermittentfastingTypes intermittentfastingType = mealPlanRequest.intermittentfastingType();



        List<Recipe> recipesByTags = recipeService.getRecipesByTags(mealPlanRequest.included(), mealPlanRequest.excluded());
        UserAccount user = userAccountService.loadUserByEmail(userEmail);
        LocalDate date = mealPlanRequest.startingDate();
        LocalDate lastdate = date.plusWeeks(mealPlanRequest.numberOfweeks());
        MasterMealPlan masterMealPlan=new MasterMealPlan();

        do {
            MealPlan mealPlanADay = new MealPlan();
            mealPlanADay.setDate(date);

            if (intermittentfastingType.getFastingDays().contains(date.getDayOfWeek())) {
                if (!intermittentfastingType.getFastingmeales().isEmpty()) {
                    for (MealType mealtype : intermittentfastingType.getFastingmeales()) {
                        Collections.shuffle(recipesByTags);
                        Meal meal = new Meal();
                        meal.setMealType(mealtype);
                        meal.setMealPlan(mealPlanADay);
                        Optional<Recipe> findAnyrecipe = recipesByTags.parallelStream()
                            .filter(r -> !mealPlanADay.containsRecipe(r))
                            .filter(r -> r.getCategory().toUpperCase().contains(mealtype.name()))
                            .findAny();
                        System.err.println("findAnyrecipe " + findAnyrecipe);
                        if (findAnyrecipe.isEmpty()) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No recipes Found for requested Plan");
                        } else {
                            if (findAnyrecipe.isPresent()) {
                                System.out.println("Recipes chose for meal " + meal + " Day " + date.getDayOfWeek() + " Recipe " + findAnyrecipe.get());
                                meal.addRecipes(findAnyrecipe.get());
                                mealPlanADay.addMeals(meal);
                            } else {

                            }
                        }

                    }
                }
            }
            if (intermittentfastingType.getNonFastingDays().contains(date.getDayOfWeek())) {
                if (!intermittentfastingType.getNonFastingmeales().isEmpty()) {
                    for (MealType mealtype : intermittentfastingType.getNonFastingmeales()) {
                        Collections.shuffle(recipesByTags);
                        Meal meal = new Meal();
                        meal.setMealType(mealtype);
                        meal.setMealPlan(mealPlanADay);
                        Optional<Recipe> findAnyrecipe = recipesByTags.parallelStream()
                            .filter(r -> !mealPlanADay.containsRecipe(r))
                            .filter(r -> r.getCategory().toUpperCase().contains(mealtype.name()))
                            .findAny();
                        System.err.println("findAnyrecipe" + findAnyrecipe);
                        if (findAnyrecipe.isEmpty() && mealPlanADay.getMeals().isEmpty()) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No recipes Found for requested Plan");
                        } else {
                            if (findAnyrecipe.isPresent()) {
                                System.out.println("Recipes chose for meal " + meal + " Day " + date.getDayOfWeek() + " Recipe " + findAnyrecipe.get());
                                meal.addRecipes(findAnyrecipe.get());
                                mealPlanADay.addMeals(meal);
                            }
                        }

                    }
                }
            }
            date = date.plusDays(1);
            masterMealPlan.addMealPlan(mealPlanADay);
        } while (date.isBefore(lastdate));

        masterMealPlan.setUserAccount(user);
        masterMealPlan.setNotes(intermittentfastingType.getDescription());
        System.out.println("-----------");
        return this.masterMealPlanRepository.save(masterMealPlan);

    }


    public void EditMealPlan() {

    }

    public MasterMealPlan getMasterPlan(String username, long masterPlanId) {
        UserAccount user = userAccountService.loadUserByEmail(username);
        Optional<MasterMealPlan> byId = this.masterMealPlanRepository.findById(masterPlanId);
        if(byId.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan not Found");
        }else{
            if (byId.get().getUserAccount().equals(user)){
                return byId.get();
            }else{
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are Unauthorized to view this plan ");
            }
        }
    }

    public ResponseEntity<Object> cancelMasterMealPlan(String username, long masterPlanId) {
        UserAccount user = userAccountService.loadUserByEmail(username);
        Optional<MasterMealPlan> byId = this.masterMealPlanRepository.findById(masterPlanId);
        if(byId.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan not Found");
        }else{
            if (byId.get().getUserAccount().equals(user)){
                this.masterMealPlanRepository.delete(byId.get());
                return ResponseEntity.noContent().build();
            }else{
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are Unauthorized to cancel this plan ");
            }
        }
    }
}
