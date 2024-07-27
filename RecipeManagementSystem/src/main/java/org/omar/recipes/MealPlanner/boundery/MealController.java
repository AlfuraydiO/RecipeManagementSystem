package org.omar.recipes.MealPlanner.boundery;


import org.omar.recipes.MealPlanner.controller.MealService;
import org.omar.recipes.MealPlanner.entity.MealPlan;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("api/meal")
public class MealController {

    MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping(value = "/{id}",produces = "application/json")
        public ResponseEntity GetMeal(@AuthenticationPrincipal UserDetails userDetails,@PathVariable long id){
        MealPlan meal = mealService.getMeal(userDetails.getUsername(), id);
        return ResponseEntity.ok(meal);
    }


    @DeleteMapping(value = "/{id}",produces = "application/json")
    public ResponseEntity DeleteMeal(@AuthenticationPrincipal UserDetails userDetails,@PathVariable long id){
        return mealService.removeMeal(userDetails.getUsername(),id);
    }

    @PutMapping(value = "/{id}",produces = "application/json")
    public ResponseEntity EditMeal(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long id, @RequestBody MealPlan mealPlan){
        mealService.editMealPlan(userDetails.getUsername(),id,mealPlan);
        return ResponseEntity.ok().build();
    }


}
