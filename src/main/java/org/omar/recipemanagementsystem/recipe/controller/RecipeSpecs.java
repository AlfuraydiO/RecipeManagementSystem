package org.omar.recipemanagementsystem.recipe.controller;

import jakarta.persistence.criteria.*;
import org.omar.recipemanagementsystem.recipe.entity.Recipe;
import org.omar.recipemanagementsystem.recipe.entity.Tag;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;

public final class RecipeSpecs {

    public static Specification<Recipe> byTagsandName(List<Integer> included, List<Integer> excluded,String name) {
      return (root, query, cb) -> {
        Predicate predicate = cb.conjunction();
        if (included != null && !included.isEmpty()) {
          predicate = cb.and(predicate,
                  tagSubquery(included, query, cb, root, false));
        }
        if (excluded != null && !excluded.isEmpty()) {
          predicate = cb.and(predicate,
                  tagSubquery(excluded, query, cb, root, true));
        }
        if(name != null && !name.isBlank()){
          predicate=cb.and(predicate,cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
          predicate=cb.and(predicate,cb.like(cb.lower(root.get("description")), "%" + name.toLowerCase() + "%"));
        }

        return predicate;
      };
    }

    private static Predicate tagSubquery(List<Integer> ids, CriteriaQuery<?> cq,
                                         CriteriaBuilder cb, Root<Recipe> root,
                                         boolean negated) {
      List<Long> tagIds = ids.stream().map(Integer::longValue).toList();
      Subquery<Long> sub = cq.subquery(Long.class);
      Root<Recipe> recipe = sub.from(Recipe.class);
      Join<Recipe, Tag> tag = recipe.join("tags");
      sub.select(recipe.get("id")).where(tag.get("id").in(tagIds));
      Predicate p = root.get("id").in(sub);
      return negated ? cb.not(p) : p;
    }


}