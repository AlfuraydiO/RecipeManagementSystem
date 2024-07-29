package org.omar.recipes.MealPlanner.controller;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import org.omar.recipes.MealPlanner.entity.MealPlan;

public interface MealPlanRepository extends CrudRepository<MealPlan, Long> {


    @Modifying
    @Query("delete from MealPlan p where p.id = ?1")
    void deleteByIdquery(long id);

}