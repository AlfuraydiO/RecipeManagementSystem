package org.omar.recipemanagementsystem.recipe.controller;

import org.omar.recipemanagementsystem.exceptions.ResourceNotFoundException;
import org.omar.recipemanagementsystem.recipe.entity.bodies.tag.TagHref;
import org.omar.recipemanagementsystem.recipe.entity.bodies.tag.TagResponse;
import org.omar.recipemanagementsystem.recipe.entity.Tag;
import org.omar.recipemanagementsystem.shareUtil.JsonUtil;
import org.omar.recipemanagementsystem.users.controller.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tools.jackson.databind.JsonNode;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class TagService {

  TagRepository tagRepository;
  UserAccountService userService;

  public TagService(TagRepository tagRepository, UserAccountService userService) {
    this.tagRepository = tagRepository;
    this.userService = userService;
  }

  public ResponseEntity<TagResponse> getTagById(long id) {
    Optional<Tag> findById = tagRepository.findById(id);
    if (findById.isPresent()) {
      return ResponseEntity.ok(crreateTagResponse(findById.get()));
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag was not found id " + id);
    }

  }

  public Optional<Tag> getTag(Tag tag) {
    return tagRepository.findByName(tag.getName());
  }

  public ResponseEntity<TagResponse> saveTag(Tag tag) {
    var tagEntity = tagRepository.save(tag);
    return ResponseEntity.created(URI.create(getTagHref(tagEntity.getId()))).body(crreateTagResponse(tagEntity));

  }

  public ResponseEntity<TagResponse> updateTag(long id, Tag tag) {
    var tagEntity = tagRepository.save(tag);
    return ResponseEntity.ok(crreateTagResponse(tagEntity));
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

  public ResponseEntity<List<TagResponse>> searchTag(String search) {

    var resutls = tagRepository.findByNameOrDescriptionContainingIgnoreCase(search, search);
    return ResponseEntity.ok(resutls.stream().map(t -> crreateTagResponse(t)).toList());

  }

  public List<TagHref> getTagsLinks(Set<Tag> tags) {
    return tags.stream()
            .map(t -> new TagHref(t.getId(), getTagHref(t.getId()))).toList();

  }

  public String getTagHref(long id) {
    return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/tag/{id}")
            .buildAndExpand(id)
            .getPath();
  }

  public ResponseEntity<TagResponse> partialUpdate(long id, JsonNode body) {
    Tag tag = tagRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id));
    if (!body.isObject()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Patch body must be present");
    }
    for (Map.Entry<String, JsonNode> field : body.properties()) {
      if (field.getKey().contains("id")||field.getKey().contains("href")) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot edit tag id/href");
      }
    }
      applyPatch(tag, body);
      Tag patchedTag = tagRepository.save(tag);
      return ResponseEntity.ok(crreateTagResponse(patchedTag));
    }


  private void applyPatch(Tag tag, JsonNode body) {
    if (body.has("name")) {
      tag.setName(JsonUtil.textValue(body.get("name"), "name"));
    }
    if (body.has("description")) {
      tag.setDescription(JsonUtil.textValue(body.get("description"), "description"));
    }
    if (body.has("type")) {
      tag.setType(JsonUtil.textValue(body.get("type"), "type"));
    }
  }

  public TagResponse crreateTagResponse(Tag tag) {
    return new TagResponse(
            String.valueOf(tag.getId()),
            getTagHref(tag.getId()),
            tag.getName(),
            tag.getDescription(),
            tag.getType());
  }
}
