package org.omar.recipemanagementsystem.recipe.entity.bodies.receipe;


import org.omar.recipemanagementsystem.recipe.entity.bodies.tag.TagHref;

import java.util.List;

/**
 * @author oalfuraydi
 */
public record RecipeRequest(
        String name,
        String description,
        String category,
        List<String> ingredients,
        List<String>  instructions,
        List<TagHref> tags) {
}
