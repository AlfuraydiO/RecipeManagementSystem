package org.omar.recipemanagementsystem.recipe.boundary;

import jakarta.validation.Valid;
import org.omar.recipemanagementsystem.recipe.controller.TagService;
import org.omar.recipemanagementsystem.recipe.entity.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/tag/")
public class TagResource {

    TagService tagService;

    public TagResource(TagService tagService) {
        this.tagService = tagService;
    }

    public record id(long id) {
    }

    @GetMapping("{id}")
    public ResponseEntity<Tag> getTag(@PathVariable Long id) {
        Tag tagById = tagService.getTagById(id);
        return ResponseEntity.ok(tagById);
    }

    @GetMapping("search/")
    public ResponseEntity<List<Tag>> searchTag(@RequestParam String search) {
        if (search.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Tag> tags = tagService.searchTag(search);
        return ResponseEntity.ok(tags);
    }
    @PatchMapping("{id}")
    public ResponseEntity partialUpdateTag(@PathVariable Long id, @RequestBody @Valid Tag tag) {
        ResponseEntity<?> responseEntity = tagService.updateTag(id, tag);
        return responseEntity;
    }
    @PutMapping("{id}")
    public ResponseEntity updateTag(@PathVariable Long id, @RequestBody @Valid Tag tag) {
        ResponseEntity<?> responseEntity = tagService.updateTag(id, tag);
        return responseEntity;
    }
    @PostMapping(value = "new", produces = "application/json")
    public ResponseEntity PostTag(@RequestBody @Valid Tag tag) {
        Optional<Tag> tag1 = tagService.saveTag(tag);
        return ResponseEntity.ok(new TagResource.id(tag1.get().getId()));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteTag(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        HttpStatus status = tagService.removeTag(id);
        return ResponseEntity.status(status.value()).build();
    }
    
}
