package org.omar.recipes.rating.boundary;

import jakarta.validation.Valid;
import org.omar.recipes.recipe.entity.Recipe;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;
import org.omar.recipes.rating.controller.RatingService;
import org.omar.recipes.rating.entity.Rating;

@RestController
@RequestMapping("api/rating/")
public class RatingController {

    RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    public record id(long id) {
    }

    @GetMapping("{id}")
    public ResponseEntity<Rating> getRating(@PathVariable Long id) {
        Rating ratingByid = ratingService.getRatingById(id);
            return ResponseEntity.ok(ratingByid); 
    }
 
    @PutMapping("{id}")
    public ResponseEntity updateRating(@AuthenticationPrincipal UserDetails userDetails,@PathVariable Long id, @RequestBody @Valid RatingRequest ratingRequest) {
        ResponseEntity<?> responseEntity = ratingService.updateRating(id, ratingRequest, userDetails.getUsername());
        return responseEntity;
    }

    @PostMapping(value = "new", produces = "application/json")
    public ResponseEntity PostRating(@AuthenticationPrincipal UserDetails userDetails,@RequestBody @Valid RatingRequest ratingRequest) {
        Optional<Rating> rating1 = ratingService.saveRecipe(ratingRequest,userDetails.getUsername());
        return ResponseEntity.ok(new id(rating1.get().getId()));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteRating(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        HttpStatus status = ratingService.removeRecipe(id,userDetails.getUsername());
         return ResponseEntity.status(status.value()).build();
    }

}
