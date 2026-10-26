package org.omar.recipemanagementsystem.MealPlanner.controller;

import org.omar.recipemanagementsystem.MealPlanner.entity.MasterMealPlan;
import org.springframework.data.repository.CrudRepository;


public interface MasterMealPlanRepository extends CrudRepository<MasterMealPlan, Long> {}