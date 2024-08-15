package org.omar.recipes.recipe.controller;

import org.omar.recipes.recipe.entity.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {

    List<Tag> findByNameOrDescriptionContainingIgnoreCase(String name, String desc);
    
    @Cacheable(value = "tagCache",key = "#name")
    Optional<Tag> findByName(String name);
}
