package org.omar.recipes.recipe.controller;

import org.omar.recipes.recipe.entity.Recipe;
import org.omar.recipes.users.controller.ChefUserService;
import org.omar.recipes.users.entity.ChefUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

   RecipeRepository recipeRepository;
    ChefUserService userService;

    public RecipeService(RecipeRepository recipeRepository, ChefUserService userService) {
        this.recipeRepository = recipeRepository;
        this.userService = userService;
    }

    public Optional<Recipe> getRecipeById(long id){
        return recipeRepository.findById(id);
     }

     public Optional<Recipe> saveRecipe(Recipe recipe,String email){
        recipe.setDate(LocalDateTime.now());
        recipe.setUser(userService.loadChefUserByEmail(email));
        return Optional.of(recipeRepository.save(recipe));
     }

    public ResponseEntity<?> updateRecipe(Long id, Recipe recipe, String email){
        Optional<Recipe> exists = recipeRepository.findById(id);
        if(exists.isPresent()){
            ChefUser chefUser = userService.loadChefUserByEmail(email);
            if(exists.get().getUser().getEmail().equals(email)){
                recipe.setDate(LocalDateTime.now());
                recipe.setId(exists.get().getId());
                recipe.setUser(chefUser);
                Recipe saved = recipeRepository.save(recipe);
                return ResponseEntity.noContent().build();
            }else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    public List<Recipe> SearchRecipe(String category, String name){
    if(!category.isEmpty()){
        return recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
    }
    if(!name.isEmpty()){
        return recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name);
    }
    return new ArrayList<Recipe>();
    }

    public HttpStatus removeRecipe(Long id, String email){
        Optional<Recipe> byId = recipeRepository.findById(id);
        if(byId.isPresent()){
            if (byId.get().getUser().getEmail().equals(email)){
                recipeRepository.delete(byId.get());
                return HttpStatus.NO_CONTENT;
            }else{
                return HttpStatus.FORBIDDEN;
            }
        }else {
            return HttpStatus.NOT_FOUND;
        }

    }
}
