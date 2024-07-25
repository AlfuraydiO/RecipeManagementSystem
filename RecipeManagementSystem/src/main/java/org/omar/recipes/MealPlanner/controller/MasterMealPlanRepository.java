package org.omar.recipes.MealPlanner.controller;

import org.omar.recipes.MealPlanner.entity.MasterMealPlan;
import org.springframework.data.repository.CrudRepository;


public interface MasterMealPlanRepository extends CrudRepository<MasterMealPlan, Long> {}