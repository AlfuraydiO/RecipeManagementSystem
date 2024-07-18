/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.omar.mealplanner.MealPlan.entity;

import com.omar.mealplanner.IntermittentFasting.DayOfWeek;
import com.omar.mealplanner.IntermittentFasting.Meal;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author oalfuraydi
 */
@Entity  
public class MealPlanDay {
    @Id
    long id;
    @Enumerated
    DayOfWeek dayOfWeek; 
    
    LocalDate date;
    
    Map<Meal, Integer> MealrecipeMap=new HashMap<>();
    
}
