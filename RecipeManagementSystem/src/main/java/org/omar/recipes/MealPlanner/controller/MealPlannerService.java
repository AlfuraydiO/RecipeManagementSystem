/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.omar.recipes.MealPlanner.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.omar.recipes.MealPlanner.entity.MealPlanDay;
import org.omar.recipes.MealPlanner.entity.enums.IntermittentfastingTypes;
import org.omar.recipes.recipe.controller.RecipeService;
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
    
    UserAccountService userAccountService;
    
    public void proposePlan(){
         IntermittentfastingTypes intermittentfastingTypes = null;
        intermittentfastingTypes = intermittentfastingTypes.AlternateDay;

        List<MealPlanDay> mealPlanDays = new ArrayList<>();
        LocalDate date = LocalDate.now();
        while(date.getDayOfWeek()!=DayOfWeek.MONDAY){
            date=date.plusDays(1);
        }
        DayOfWeek dayOfWeekTest = date.getDayOfWeek();
        System.err.println("day: " + dayOfWeekTest);
         System.err.println("date: " + date.toString()
            );
        for (DayOfWeek dayOfWeek : intermittentfastingTypes.getFastingDays()) {
            System.err.println("Fasting " + dayOfWeek);
        }
        for (DayOfWeek dayOfWeek : intermittentfastingTypes.getNonFastingDays()) {
            System.err.println("Not Fasting " + dayOfWeek);
        }
        
    }
    
    public void ConfirmPlan(){
        
    }
    
    
    public void ChangeStartingDate(){
        
    }
    
    
    
    
}
