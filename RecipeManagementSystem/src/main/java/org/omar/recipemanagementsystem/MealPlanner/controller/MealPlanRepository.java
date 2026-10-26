package org.omar.recipemanagementsystem.MealPlanner.controller;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import org.omar.recipemanagementsystem.MealPlanner.entity.MealPlan;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MealPlanRepository extends CrudRepository<MealPlan, Long> , JpaSpecificationExecutor<MealPlan> {


    @Modifying
    @Query("delete from MealPlan p where p.id = ?1")
    void deleteByIdquery(long id);
    
}