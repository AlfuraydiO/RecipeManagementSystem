/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.omar.mealplanner.IntermittentFasting;

/**
 *
 * @author oalfuraydi
 */
public enum DayOfWeek {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;
    
    private int numberOfMeal;

    private DayOfWeek(int numberOfMeal) {
        this.numberOfMeal = numberOfMeal;
    }

    private DayOfWeek() {
    }

    public int getNumberOfMeal() {
        return numberOfMeal;
    }

    public void setNumberOfMeal(int numberOfMeal) {
        this.numberOfMeal = numberOfMeal;
    }
    
    
}
