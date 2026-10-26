package org.omar.recipemanagementsystem.recipe.controller;

import org.omar.recipemanagementsystem.recipe.entity.bodies.tag.TagHref;
import org.omar.recipemanagementsystem.recipe.entity.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TagService {

    TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag getTagById(long id) {
        Optional<Tag> findById = tagRepository.findById(id);
        if (findById.isPresent()) {
            return findById.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag was not found id " + id);
        }

    }

    public Optional<Tag> getTag(Tag tag) {
        return tagRepository.findByName(tag.getName());
    }

    public Optional<Tag> saveTag(Tag tag) {
        return Optional.of(tagRepository.save(tag));
    }

    public ResponseEntity updateTag(long id, Tag tag) {
        return ResponseEntity.ok(Optional.of(tagRepository.save(tag)).get());
    }

    public HttpStatus removeTag(Long id) {
        Optional<Tag> byId = tagRepository.findById(id);
        if (byId.isPresent()) {
            tagRepository.delete(byId.get());
            return HttpStatus.NO_CONTENT;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag was not found id " + id);
        }

    }

    public List<Tag> searchTag(String search) {
        return tagRepository.findByNameOrDescriptionContainingIgnoreCase(search, search);
    }

    public List<TagHref> getTagsLinks(Set<Tag> tags) {
        return tags.stream()
                .map(t->new TagHref(t.getId(),getTagHref(t.getId()))).toList();

    }

    public String getTagHref(long id){
        return  ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/tag/{id}")
                .buildAndExpand(id)
                .getPath();
    }
}
