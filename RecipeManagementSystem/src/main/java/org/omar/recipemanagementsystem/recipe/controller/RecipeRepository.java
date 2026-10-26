package org.omar.recipemanagementsystem.recipe.controller;

import org.omar.recipemanagementsystem.recipe.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecipeRepository extends CrudRepository<Recipe, Long> , JpaSpecificationExecutor<Recipe> {

    //List<Recipe> findByCategoryIgnoreCaseOrderByDateDesc(String category);

    List<Recipe> findByNameContainingIgnoreCaseOrderByDateDesc(String name);

    Page<Recipe> findByNameContainingIgnoreCaseOrderByDateDesc(String name, Pageable pageable);


    boolean existsByName(String name);
}