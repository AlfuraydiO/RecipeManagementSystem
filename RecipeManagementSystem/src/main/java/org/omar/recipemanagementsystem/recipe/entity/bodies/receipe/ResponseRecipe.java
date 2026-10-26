package org.omar.recipemanagementsystem.recipe.entity.bodies.receipe;


  import org.omar.recipemanagementsystem.recipe.entity.bodies.tag.TagHref;
  import org.omar.recipemanagementsystem.users.entity.user.UserAccountHref;

  import java.time.LocalDate;
import java.util.List;

public record ResponseRecipe(long id, String href,
                             String name,
                             String description, String category, List<String> instructions, List<String>ingredients, LocalDate date,
                             UserAccountHref createdBy,
                             List<TagHref> tags){}