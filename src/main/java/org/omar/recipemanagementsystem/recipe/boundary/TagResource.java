package org.omar.recipemanagementsystem.recipe.boundary;

import jakarta.validation.Valid;
import org.omar.recipemanagementsystem.recipe.controller.TagService;
import org.omar.recipemanagementsystem.recipe.entity.Tag;
import org.omar.recipemanagementsystem.recipe.entity.bodies.tag.TagResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "api/tag", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagResource {

  TagService tagService;

  public TagResource(TagService tagService) {
    this.tagService = tagService;
  }

  @GetMapping("{id}")
  public ResponseEntity<TagResponse> getTag(@PathVariable long id) {
    return tagService.getTagById(id);
  }


  @GetMapping()
  public ResponseEntity<List<TagResponse>> searchTag(@RequestParam String search) {
    if (search.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    return tagService.searchTag(search);
  }

  @PutMapping("{id}")
  public ResponseEntity<?> updateTag(@PathVariable long id, @RequestBody @Valid Tag tag) {
    return tagService.updateTag(id, tag);
  }

  @PostMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<TagResponse> PostTag(@RequestBody @Valid Tag tag) {
    return tagService.saveTag(tag);
  }

  @PatchMapping("{id}")
  public ResponseEntity<TagResponse> patchTag(@PathVariable long id, @RequestBody JsonNode jsonNode) {
    return tagService.partialUpdate(id, jsonNode);
  }

  @DeleteMapping("{id}")
  public ResponseEntity deleteTag(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
    HttpStatus status = tagService.removeTag(id);
    return ResponseEntity.status(status.value()).build();
  }

}
