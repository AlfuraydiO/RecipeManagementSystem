package org.omar.recipes.recipe.boundary.requestAndResponseBodies;

import java.util.List;

/**
 * @author oalfuraydi
 */
public record RecipeRequest(
        String name,
        String description,
        String category,
        List<String> ingredients,
        List<String> directions,
        int[] tags) {
}
