package org.omar.recipes.rating.controller;

import org.omar.recipes.rating.entity.Rating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends CrudRepository<Rating, Long> {

    @Query("select r from Rating r where r.recipe.id = :recipeid")
    List<Rating> findRatingByRecipe(@Param("recipeid") long id);

    @Query("select COUNT(*) from Rating r where r.recipe.id = :recipeid")
    long countRatingByRecipe(@Param("recipeid") long id);

}
