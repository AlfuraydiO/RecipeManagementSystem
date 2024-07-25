
package org.omar.recipes.MealPlanner.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
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
import org.springframework.stereotype.Service;

/**
 *
 * @author oalfuraydi
 */
@Service
public class MealPlannerService {

    RecipeService recipeService;
    MealPlanRepository mealPlanRepository;
    UserAccountService userAccountService;

    public MealPlannerService(RecipeService recipeService, MealPlanRepository mealPlanRepository, UserAccountService userAccountService) {
        this.recipeService = recipeService;
        this.mealPlanRepository = mealPlanRepository;
        this.userAccountService = userAccountService;
    }

    public List<MealPlan> RequestPlan(String userEmail, MealPlanRequest mealPlanRequest) {
        System.err.println(mealPlanRequest.intermittentfastingType());
        IntermittentfastingTypes intermittentfastingType = mealPlanRequest.intermittentfastingType();

        List<MealPlan> mealPlanDays = new ArrayList<>();

        List<Recipe> recipesByTags = recipeService.getRecipesByTags(mealPlanRequest.included(), mealPlanRequest.excluded());
        UserAccount user = userAccountService.loadUserByEmail(userEmail);
        LocalDate date = mealPlanRequest.startingDate();
        LocalDate lastdate = date.plusWeeks(mealPlanRequest.numberOfweeks());
        do {
            MealPlan mealPlanADay = new MealPlan();
            mealPlanADay.setDate(date);
            mealPlanADay.setNotes(intermittentfastingType.getDescription());
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
                            throw new RuntimeException("No meal found for plan");
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
                            throw new RuntimeException("No meal found for plan");
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
            mealPlanDays.add(mealPlanADay);
            System.out.println("Day "+mealPlanDays.size()+" Date "+date);
            //this.mealPlanRepository.save(mealPlanADay);

        } while (date.isBefore(lastdate));
        MasterMealPlan masterMealPlan=new MasterMealPlan();
        masterMealPlan.setMealPlans(mealPlanDays);
        masterMealPlan.setUserAccount(user);
        System.out.println("-----------");

        return mealPlanDays;

    }


    public void EditMealPlan() {

    }

    public void cancelMealPlan() {

    }


}
