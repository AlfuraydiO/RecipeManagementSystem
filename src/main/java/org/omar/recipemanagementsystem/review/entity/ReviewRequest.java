
package org.omar.recipemanagementsystem.review.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * @author oalfuraydi
 */
public record ReviewRequest(@NotNull(message = "Recipe id is mandatory") long recipeId,
                            @Min(value = 1, message = "Review must be larger than 0")
                            @Max(value = 10, message = "Review must be no larger than 10") int rating,
                            String review) {
}
