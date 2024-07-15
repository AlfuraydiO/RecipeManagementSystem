package org.omar.recipes.recipe.boundary;

import org.omar.recipes.recipe.boundary.requestAndResponseBodies.RecipeRequest;
import org.omar.recipes.recipe.boundary.requestAndResponseBodies.idResponse;
import org.omar.recipes.recipe.controller.RecipeService;
import org.omar.recipes.recipe.entity.Recipe;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/recipe/")
public class RecipeController {

    RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long id) {
        Optional<Recipe> recipeById = recipeService.getRecipeById(id);
        if (recipeById.isPresent()) {
            return ResponseEntity.ok(recipeById.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe " + id + " Not Found !");
        }

    }

    @GetMapping("search/")
    public ResponseEntity<List<Recipe>> searchRecipe( @RequestParam(defaultValue = "", required = false) String name) {
        if (name.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Recipe> recipes = recipeService.SearchRecipe(name);
        return ResponseEntity.ok(recipes);
    }

    @PutMapping("{id}")
    public ResponseEntity updateRecipe(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody RecipeRequest recipe) {
        ResponseEntity<?> responseEntity = recipeService.updateRecipe(id, recipe, userDetails.getUsername());
        return responseEntity;
    }

    @PostMapping(value = "new", produces = "application/json")
    public ResponseEntity PostRecipe(@AuthenticationPrincipal UserDetails userDetails, @RequestBody RecipeRequest recipe) {
        Recipe recipe1 = recipeService.saveRecipe(recipe, userDetails.getUsername());
        return ResponseEntity.ok(new idResponse(recipe1.getId()));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteRecipe(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        HttpStatus status = recipeService.removeRecipe(id, userDetails.getUsername());
        return ResponseEntity.status(status.value()).build();
    }

}
