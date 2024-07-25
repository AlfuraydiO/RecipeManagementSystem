/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package org.omar.recipes.MealPlanner.entity.enums;

import org.omar.recipes.MealPlanner.entity.enums.MealType;
import java.time.DayOfWeek;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;


public enum IntermittentfastingTypes {
    Hours16Till8(EnumSet.allOf(DayOfWeek.class),
        Set.of(), List.of(MealType.LUNCH, MealType.DINNER), List.of(),
    "Fast for 16 hours and eat within an 8-hour window each day."),
    
    Days5OffAnd2On(EnumSet.of(DayOfWeek.MONDAY,  DayOfWeek.WEDNESDAY), 
        Set.of(DayOfWeek.TUESDAY,DayOfWeek.SATURDAY,DayOfWeek.FRIDAY,DayOfWeek.THURSDAY, DayOfWeek.SUNDAY),
         List.of(MealType.LUNCH, MealType.DINNER), List.of(MealType.BREAKFAST, MealType.LUNCH, MealType.DINNER),
    "Eat normally for 5 days and restrict calories (500-600) on 2 non-consecutive days."),

    AlternateDay(EnumSet.of(DayOfWeek.MONDAY,  DayOfWeek.WEDNESDAY,  DayOfWeek.FRIDAY,DayOfWeek.SUNDAY), 
        Set.of(DayOfWeek.TUESDAY,DayOfWeek.THURSDAY,DayOfWeek.SATURDAY),
         List.of(MealType.LUNCH, MealType.DINNER), 
      List.of(MealType.BREAKFAST, MealType.LUNCH, MealType.DINNER),"Fast every other day"),
    
    OneMealaDay(EnumSet.allOf(DayOfWeek.class), 
        Set.of(),
         List.of(MealType.DINNER,MealType.DINNER), 
      List.of(),"One Large Meal a day"),
    
     TwelveTillTwelve(EnumSet.allOf(DayOfWeek.class),
        Set.of(), List.of(MealType.LUNCH, MealType.DINNER), List.of()
         ,"Fast for 12 hours and eat within a 12-hour window each day.");
     
    //Consider a set for the Number of days since we can exlicly deifned which days;
    private Set<DayOfWeek> fastingDays;
    private Set<DayOfWeek> nonFastingDays;
    private List<MealType> Fastingmeales;
    private List<MealType> nonFastingmeales;
    private String description;

     
    private IntermittentfastingTypes(Set<DayOfWeek> fastingDays, Set<DayOfWeek> nonFastingDays, List<MealType> Fastingmeales, List<MealType> nonFastingmeales, String description) {
        this.fastingDays = fastingDays;
        this.nonFastingDays = nonFastingDays;
        this.Fastingmeales = Fastingmeales;
        this.nonFastingmeales = nonFastingmeales;
        this.description = description;
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

    public List<MealType> getFastingmeales() {
        return Fastingmeales;
    }

    public void setFastingmeales(List<MealType> Fastingmeales) {
        this.Fastingmeales = Fastingmeales;
    }

    public List<MealType> getNonFastingmeales() {
        return nonFastingmeales;
    }

    public void setNonFastingmeales(List<MealType> nonFastingmeales) {
        this.nonFastingmeales = nonFastingmeales;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "IntermittentfastingTypes{" + "ordinal=" + ordinal() + ", name=" + name() + ", fastingDays=" + fastingDays + ", nonFastingDays=" + nonFastingDays + ", Fastingmeales=" + Fastingmeales + ", nonFastingmeales=" + nonFastingmeales + ", desvribtion=" + description + '}';
    }
    
    

}
