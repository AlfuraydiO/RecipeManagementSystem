package org.omar.recipes.recipe.controller;

import org.omar.recipes.recipe.entity.Recipe;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Cacheable(value = "stringCache")
public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    //List<Recipe> findByCategoryIgnoreCaseOrderByDateDesc(String category);

    List<Recipe> findByNameContainingIgnoreCaseOrderByDateDesc(String name);
    
    @Query(value = """
           SELECT * FROM RECIPE AS r
           WHERE r.ID IN (
             SELECT rt.RECIPE_ID FROM RECIPE_TAGS AS rt
             WHERE rt.TAG_ID IN (:included)
           ) AND r.ID NOT IN (
             SELECT rt.RECIPE_ID FROM RECIPE_TAGS AS rt
             WHERE rt.TAG_ID IN (:excluded)) 
           """,nativeQuery = true)
    List<Recipe> findRecipesByTagsParam(@Param("included") List<Integer> included,@Param("excluded")List<Integer> excluded);
    
}