package org.omar.recipemanagementsystem.recipe.boundary;

import tools.jackson.databind.JsonNode;
import org.omar.recipemanagementsystem.recipe.entity.bodies.receipe.RecipeRequest;
import org.omar.recipemanagementsystem.recipe.entity.bodies.receipe.ResponseRecipe;
import org.omar.recipemanagementsystem.recipe.controller.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "api/recipe", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class RecipeResource {

  private static final Logger log = LoggerFactory.getLogger(RecipeResource.class);
  RecipeService recipeService;
  ApplicationContext applicationContext;

  public RecipeResource(RecipeService recipeService, ApplicationContext applicationContext) {
    this.recipeService = recipeService;
    this.applicationContext = applicationContext;
  }


  @GetMapping("{id}")
  public ResponseEntity<ResponseRecipe> getRecipe(@PathVariable Long id) {
    log.info("name " + id);
    return recipeService.getRecipeById(id);

  }

  @GetMapping
  public ResponseEntity<List<ResponseRecipe>> getRecipeByTags(@RequestParam(defaultValue = "", required = false) String name,
                                                              @RequestParam(required = false) String includedTags,
                                                              @RequestParam(required = false) String excludeTags,
                                                              @RequestParam(defaultValue = "0", required = false) int offset, @RequestParam(defaultValue = "5", required = false) int limit) {
    if (limit == 0) {
      return ResponseEntity.badRequest().body(List.of());
    }
    List<Integer> includedTagslist = List.of();
    List<Integer> excludedTagList = List.of();
    if (includedTags != null && !includedTags.isBlank()) {
      String[] itags = includedTags.split(",");
      includedTagslist = Arrays.stream(itags).map(Integer::valueOf).toList();
    }
    if (excludeTags != null && !excludeTags.isBlank()) {
      String[] etags = excludeTags.split(",");
      excludedTagList = Arrays.stream(etags).map(Integer::valueOf).toList();
    }

    Page<ResponseRecipe> recipes = recipeService.getRecipesByTagsAndNamePagination(includedTagslist,
            excludedTagList, offset, limit, name);
    if (recipes.getTotalElements() > 0 && !recipes.isEmpty()) {
      return ResponseEntity.status(206).header("X-Total-Count", String.valueOf(recipes.getTotalElements()))
              .header("X-Total-Pages", String.valueOf(recipes.getTotalPages())).body(recipes.stream().toList());

    } else if (recipes.getTotalElements() > 0) {
      return ResponseEntity.status(416).header("X-Total-Count", String.valueOf(recipes.getTotalElements()))
              .header("X-Total-Pages", String.valueOf(recipes.getTotalPages())).body(recipes.stream().toList());
    } else {
      return ResponseEntity.status(404).header("X-Total-Count", String.valueOf(recipes.getTotalElements()))
              .header("X-Total-Pages", String.valueOf(recipes.getTotalPages())).body(recipes.stream().toList());
    }
  }

  @GetMapping("search/")
  public ResponseEntity<List<ResponseRecipe>> searchRecipePagination(@RequestParam(defaultValue = "", required = false) String name
          , @RequestParam(defaultValue = "0", required = false) int offset, @RequestParam(defaultValue = "5", required = false) int limit) {
    if (offset == 0 && limit == 0) {
      List<ResponseRecipe> recipes = recipeService.SearchRecipe(name);
      return ResponseEntity.ok(recipes);
    } else {
      if (limit == 0) {
        return ResponseEntity.badRequest().body(List.of());
      }
      Page<ResponseRecipe> recipes = recipeService.SearchRecipe(name, offset, limit);
      return ResponseEntity.status(206).header("X-Total-Count", String.valueOf(recipes.getTotalElements()))
              .header("X-Total-Pages", String.valueOf(recipes.getTotalPages())).body(recipes.stream().toList());
    }
  }


  @PutMapping("{id}")
  public ResponseEntity<ResponseRecipe> updateRecipe(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id,
                                                     @RequestBody RecipeRequest recipe) {
    return recipeService.updateRecipe(id, recipe, userDetails.getUsername());
  }


  @PatchMapping(value = "{id}", consumes = "application/merge-patch+json",
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseRecipe> partialUpdateRecipe(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id,
                                            @RequestBody JsonNode recipeUpdate) {

    return recipeService.partialUpdateRecipe(id, recipeUpdate, userDetails.getUsername());
  }

  @PostMapping
  public ResponseEntity<ResponseRecipe> PostRecipe(@AuthenticationPrincipal UserDetails userDetails,
                                                   @RequestBody RecipeRequest recipe) {

    return recipeService.saveRecipe(recipe, userDetails.getUsername());
  }

  @DeleteMapping("{id}")
  public ResponseEntity deleteRecipe(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
    HttpStatus status = recipeService.removeRecipe(id, userDetails.getUsername());
    return ResponseEntity.status(status.value()).build();
  }

}
