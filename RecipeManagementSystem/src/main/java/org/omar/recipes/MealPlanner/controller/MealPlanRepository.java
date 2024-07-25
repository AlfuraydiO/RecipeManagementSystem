package org.omar.recipes.MealPlanner.controller;

import org.springframework.data.repository.CrudRepository;

import org.omar.recipes.MealPlanner.entity.MealPlan;

public interface MealPlanRepository extends CrudRepository<MealPlan, Long> {

    
    
}