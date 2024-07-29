/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.omar.recipes.MealPlanner.boundery;

import jakarta.validation.Valid;
import java.util.List;
import org.omar.recipes.MealPlanner.boundery.RequestAndResponseBodies.MealPlanRequest;
import org.omar.recipes.MealPlanner.controller.MealPlannerService;
import org.omar.recipes.MealPlanner.entity.MasterMealPlan;
import org.omar.recipes.MealPlanner.entity.MealPlan;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/mealplanner")
public class MealPlannerController {
    
    MealPlannerService mealPlannerService;
    
    public MealPlannerController(MealPlannerService mealPlannerService) {
        this.mealPlannerService = mealPlannerService;
    }
    
    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity RequestMasterMealPla(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid MealPlanRequest mealPlanRequest) {
        MasterMealPlan masterMealPlan = mealPlannerService.RequestPlan(userDetails.getUsername(), mealPlanRequest);
        return ResponseEntity.ok(masterMealPlan);
    }
    
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity CancenlMasterMealPla(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long id) {
        return mealPlannerService.cancelMasterMealPlan(userDetails.getUsername(), id);

    }
    
      @GetMapping(value = "/{id}/", produces = "application/json")
    public ResponseEntity GetMasterMealPlan(@AuthenticationPrincipal UserDetails userDetails,  @PathVariable long id) {
        MasterMealPlan masterMealPlan = mealPlannerService.getMasterPlan(userDetails.getUsername(), id);
        return ResponseEntity.ok(masterMealPlan);
    }

}
