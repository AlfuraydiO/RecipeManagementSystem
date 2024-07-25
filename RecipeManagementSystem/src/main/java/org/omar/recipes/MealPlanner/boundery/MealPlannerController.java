/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.omar.recipes.MealPlanner.boundery;

import jakarta.validation.Valid;
import java.util.List;
import org.omar.recipes.MealPlanner.boundery.RequestAndResponseBodies.MealPlanRequest;
import org.omar.recipes.MealPlanner.controller.MealPlannerService;
import org.omar.recipes.MealPlanner.entity.MealPlan;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 
@RestController
@RequestMapping("api/mealplanner/")
public class MealPlannerController {
    
    MealPlannerService mealPlannerService;
    
    public MealPlannerController(MealPlannerService mealPlannerService) {
        this.mealPlannerService = mealPlannerService;
    }
    
    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity RequestMealPlan(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid MealPlanRequest mealPlanRequest) {
        List<MealPlan> mealPlan = mealPlannerService.RequestPlan(userDetails.getUsername(), mealPlanRequest);
        return ResponseEntity.ok(mealPlan);
    }
    
    @DeleteMapping(value = "/", produces = "application/json")
    public ResponseEntity CancenlPlan(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid  List<MealPlan> mealPlanList) {
    
        return ResponseEntity.ok().build();
    }
    
      @GetMapping(value = "/{id}/", produces = "application/json")
    public ResponseEntity GetMealPlan(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid MealPlanRequest mealPlanRequest) {
        List<MealPlan> mealPlan = mealPlannerService.RequestPlan(userDetails.getUsername(), mealPlanRequest);
        return ResponseEntity.ok(mealPlan);
    }
    
      @PostMapping(value = "/{id}/", produces = "application/json")
    public ResponseEntity EditMealPlan(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid MealPlanRequest mealPlanRequest) {
       // List<MealPlan> mealPlan = mealPlannerService.RequestPlan(userDetails.getUsername(), mealPlanRequest);
        return ResponseEntity.ok().build();
    }
    
   
}
