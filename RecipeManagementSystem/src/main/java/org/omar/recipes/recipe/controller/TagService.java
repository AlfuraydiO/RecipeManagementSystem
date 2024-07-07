package org.omar.recipes.recipe.controller;

import org.omar.recipes.recipe.entity.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }
    
    public Optional<Tag> getTagById(long id){
        return tagRepository.findById(id);
    }

    public Optional<Tag> getTag(Tag tag){
        return tagRepository.findByName(tag.getName());
    }

    public Optional<Tag> saveTag(Tag tag){
        return Optional.of(tagRepository.save(tag));
    }

    public ResponseEntity updateTag(long id,Tag tag){
        return ResponseEntity.ok(Optional.of(tagRepository.save(tag)).get());
    }

    public HttpStatus removeTag(Long id){
        Optional<Tag> byId = tagRepository.findById(id);
        if(byId.isPresent()){
                tagRepository.delete(byId.get());
                return HttpStatus.NO_CONTENT;
        }else {
            return HttpStatus.NOT_FOUND;
        }

    }

    public List<Tag> searchTag(String search) {
        return tagRepository.findByNameOrDescriptionContainingIgnoreCase(search,search);
    }
}
