
package org.omar.recipes.rating.boundary;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * @author oalfuraydi
 */
public record RatingRequest(@NotNull(message = "Recipe id is mandatory") long recipeId,
                            @Min(value = 1, message = "Rating must be larger than 0")
                            @Max(value = 10, message = "Rating must be no larger than 10") double rating,
                            String review) {
}
