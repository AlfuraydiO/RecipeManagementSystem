package org.omar.recipes.recipe.boundary;

import jakarta.validation.Valid;
import org.omar.recipes.recipe.controller.RecipeService;
import org.omar.recipes.recipe.entity.Recipe;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/recipe/")
public class RecipeController {

    RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    public record id(long id) {
    }

    @GetMapping("{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long id) {
        Optional<Recipe> recipeById = recipeService.getRecipeById(id);
        if (recipeById.isPresent()) {
            return ResponseEntity.ok(recipeById.get());
        }
        return ResponseEntity.status(404).build();
    }

    @GetMapping("search/")
    public ResponseEntity<List<Recipe>> searchRecipe(@RequestParam(required = false, defaultValue = "") String category, @RequestParam(defaultValue = "", required = false) String name) {
        if (category.isEmpty() && name.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Recipe> recipes = recipeService.SearchRecipe(category, name);
        return ResponseEntity.ok(recipes);
    }

    @PutMapping("{id}")
    public ResponseEntity updateRecipe(@AuthenticationPrincipal UserDetails userDetails,@PathVariable Long id, @RequestBody @Valid Recipe recipe) {
        ResponseEntity<?> responseEntity = recipeService.updateRecipe(id, recipe, userDetails.getUsername());
        return responseEntity;
    }

    @PostMapping(value = "new", produces = "application/json")
    public ResponseEntity PostRecipe(@AuthenticationPrincipal UserDetails userDetails,@RequestBody @Valid Recipe recipe) {
        Optional<Recipe> recipe1 = recipeService.saveRecipe(recipe,userDetails.getUsername());
        return ResponseEntity.ok(new id(recipe1.get().getId()));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteRecipe(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        HttpStatus status = recipeService.removeRecipe(id,userDetails.getUsername());
         return ResponseEntity.status(status.value()).build();
    }

}
