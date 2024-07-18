/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package org.omar.recipes.MealPlanner.entity.enums;

import org.omar.recipes.MealPlanner.entity.enums.Meal;
import java.time.DayOfWeek;
import java.util.EnumSet;
import java.util.Set;


public enum IntermittentfastingTypes {
    Hours16Till8(EnumSet.allOf(DayOfWeek.class),
        Set.of(), Set.of(Meal.LANUCH, Meal.DINNER), Set.of(),
    "Fast for 16 hours and eat within an 8-hour window each day."),
    
    Days5OffAnd2On(EnumSet.of(DayOfWeek.MONDAY,  DayOfWeek.WEDNESDAY), 
        Set.of(DayOfWeek.TUESDAY,DayOfWeek.SATURDAY,DayOfWeek.FRIDAY,DayOfWeek.THURSDAY, DayOfWeek.SUNDAY),
         Set.of(Meal.LANUCH, Meal.DINNER), Set.of(Meal.BREAKFAST, Meal.LANUCH, Meal.DINNER),
    "Eat normally for 5 days and restrict calories (500-600) on 2 non-consecutive days."),

    AlternateDay(EnumSet.of(DayOfWeek.MONDAY,  DayOfWeek.WEDNESDAY,  DayOfWeek.FRIDAY,DayOfWeek.SUNDAY), 
        Set.of(DayOfWeek.TUESDAY,DayOfWeek.THURSDAY,DayOfWeek.SATURDAY),
         Set.of(Meal.LANUCH, Meal.DINNER), 
      Set.of(Meal.BREAKFAST, Meal.LANUCH, Meal.DINNER),"Fast every other day"),
    
    OneMealaDay(EnumSet.allOf(DayOfWeek.class), 
        Set.of(),
         Set.of(Meal.DINNER), 
      Set.of(),"One Large Meal a day"),
    
     TwelveTillTwelve(EnumSet.allOf(DayOfWeek.class),
        Set.of(), Set.of(Meal.LANUCH, Meal.DINNER), Set.of()
         ,"Fast for 12 hours and eat within a 12-hour window each day.");
     
    //Consider a set for the Number of days since we can exlicly deifned which days;
    private Set<DayOfWeek> fastingDays;
    private Set<DayOfWeek> nonFastingDays;
    private Set<Meal> Fastingmeales;
    private Set<Meal> nonFastingmeales;
    private String desvribtion;

     
    private IntermittentfastingTypes(Set<DayOfWeek> fastingDays, Set<DayOfWeek> nonFastingDays, Set<Meal> Fastingmeales, Set<Meal> nonFastingmeales, String desvribtion) {
        this.fastingDays = fastingDays;
        this.nonFastingDays = nonFastingDays;
        this.Fastingmeales = Fastingmeales;
        this.nonFastingmeales = nonFastingmeales;
        this.desvribtion = desvribtion;
    }

    public Set<DayOfWeek> getFastingDays() {
        return fastingDays;
    }

    public void setFastingDays(Set<DayOfWeek> fastingDays) {
        this.fastingDays = fastingDays;
    }

    public Set<DayOfWeek> getNonFastingDays() {
        return nonFastingDays;
    }

    public void setNonFastingDays(Set<DayOfWeek> nonFastingDays) {
        this.nonFastingDays = nonFastingDays;
    }

    public Set<Meal> getFastingmeales() {
        return Fastingmeales;
    }

    public void setFastingmeales(Set<Meal> Fastingmeales) {
        this.Fastingmeales = Fastingmeales;
    }

    public Set<Meal> getNonFastingmeales() {
        return nonFastingmeales;
    }

    public void setNonFastingmeales(Set<Meal> nonFastingmeales) {
        this.nonFastingmeales = nonFastingmeales;
    }

    public String getDesvribtion() {
        return desvribtion;
    }

    public void setDesvribtion(String desvribtion) {
        this.desvribtion = desvribtion;
    }

}
