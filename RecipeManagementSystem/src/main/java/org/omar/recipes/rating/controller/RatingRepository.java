package org.omar.recipes.rating.controller;

import org.springframework.data.repository.CrudRepository;
import org.omar.recipes.rating.entity.Rating;

public interface RatingRepository extends CrudRepository<Rating, Long> {

 
}