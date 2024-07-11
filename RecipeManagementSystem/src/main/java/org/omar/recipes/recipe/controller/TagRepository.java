package org.omar.recipes.recipe.controller;

import org.omar.recipes.recipe.entity.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {

    List<Tag> findByNameOrDescriptionContainingIgnoreCase(String name, String desc);

    Optional<Tag> findByName(String name);
}
