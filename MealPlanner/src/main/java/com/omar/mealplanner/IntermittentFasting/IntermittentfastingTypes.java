/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.omar.mealplanner.IntermittentFasting;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author oalfuraydi
 */
public enum IntermittentfastingTypes {
    Hours16Till8(2,7,EnumSet.of(Meal.LANUCH,Meal.DINNER,Meal.SNACK),false),
    Days5OnAnd2Off(2,5,EnumSet.of(Meal.BREAKFAST,Meal.LANUCH,Meal.DINNER,Meal.SNACK),true),
    AlternateDay(0,3,new HashSet<Meal>(),true),
    OMaD(1,7,EnumSet.of(Meal.BREAKFAST,Meal.LANUCH,Meal.DINNER,Meal.SNACK),false),
    TwelveTillTwelve(2,7,EnumSet.of(Meal.LANUCH,Meal.BREAKFAST,Meal.DINNER,Meal.SNACK),true);
    
    private int NumberOfMeals;
    //Consider a set for the Number of days since we can exlicly deifned which days;
    private int daysOfTheWeek;
    private Set<Meal> meales =new HashSet<>() ;
    private boolean isAlternate;

    private IntermittentfastingTypes(int NumberOfMeal, int daysOfTheWeek, Set<Meal> meales,boolean isAlternate) {
        this.NumberOfMeals = NumberOfMeal;
        this.daysOfTheWeek = daysOfTheWeek;
        this.meales = meales;
        this.isAlternate=isAlternate;
    }
    
    

    public int getNumberOfMeal() {
        return NumberOfMeals;
    }

    public void setNumberOfMeal(int NumberOfMeal) {
        this.NumberOfMeals = NumberOfMeal;
    }

    public int getDaysOfTheWeek() {
        return daysOfTheWeek;
    }

    public void setDaysOfTheWeek(int daysOfTheWeek) {
        this.daysOfTheWeek = daysOfTheWeek;
    }

    public Set<Meal> getMeales() {
        return meales;
    }

    public void setMeales(Set<Meal> meales) {
        this.meales = meales;
    } 
}
