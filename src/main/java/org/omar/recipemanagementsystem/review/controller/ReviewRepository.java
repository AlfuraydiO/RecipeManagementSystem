package org.omar.recipemanagementsystem.review.controller;

import org.omar.recipemanagementsystem.review.entity.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends CrudRepository<Review, Long> {

  @Query("select r from Review r where r.recipe.id = :recipeid")
  List<Review> findReviewByRecipe(@Param("recipeid") long id);

  @Query("select COUNT(*) from Review r where r.recipe.id = :recipeid")
  long countReviewByRecipe(@Param("recipeid") long id);

  @Query("select r from Review r where r.recipe.id = :recipeId and r.userAccount.id =:userId")
  Optional<Review> findReviewByRecipeandUser(@Param("recipeId") long recipeId, @Param("userId") long userId);

  @Query("select  SUM(r.rating) from Review r where r.recipe.id = :recipeId ")
  long getSumOfRatingsByRecipe(long recipeId);
}
