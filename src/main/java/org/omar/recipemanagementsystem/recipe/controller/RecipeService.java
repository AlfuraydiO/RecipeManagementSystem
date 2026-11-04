package org.omar.recipemanagementsystem.recipe.controller;

import org.omar.recipemanagementsystem.review.entity.Review;
import tools.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.net.URI;
import java.time.LocalDate;

import org.omar.recipemanagementsystem.exceptions.ResourceNotFoundException;
import org.omar.recipemanagementsystem.review.controller.ReviewService;
import org.omar.recipemanagementsystem.recipe.entity.bodies.receipe.RecipeRequest;
import org.omar.recipemanagementsystem.recipe.entity.bodies.tag.TagHref;
import org.omar.recipemanagementsystem.recipe.entity.bodies.receipe.ResponseRecipe;
import org.omar.recipemanagementsystem.recipe.entity.Recipe;
import org.omar.recipemanagementsystem.recipe.entity.Tag;
import org.omar.recipemanagementsystem.users.controller.UserAccountService;
import org.omar.recipemanagementsystem.users.entity.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.omar.recipemanagementsystem.shareUtil.JsonUtil.stringList;
import static org.omar.recipemanagementsystem.shareUtil.JsonUtil.textValue;

@Service
@Transactional
public class RecipeService {

  private static final Logger log = LoggerFactory.getLogger(RecipeService.class);
  private final RecipeRepository recipeRepository;
  private final UserAccountService userService;
  private final TagService tagService;
  private final TagRepository tagRepository;
  private final Validator validator;
  private ReviewService reviewService;

  private static final Set<String> IMMUTABLE_RECIPE_FIELDS = Set.of("id", "date", "createdBy", "totalRating", "href");

  public RecipeService(RecipeRepository recipeRepository, UserAccountService userService, TagService tagService,
      TagRepository tagRepository,
      Validator validator) {
    this.recipeRepository = recipeRepository;
    this.userService = userService;
    this.tagService = tagService;
    this.validator = validator;
    this.tagRepository = tagRepository;
  }

  public ResponseEntity<ResponseRecipe> getRecipeById(long id) {

    var recipeEntityOoptional = recipeRepository.findById(id);
    if (recipeEntityOoptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe " + id + " Not Found !");
    }
    var recipeEntity = recipeEntityOoptional.get();

    var recipeResponse = createRecipeResponse(recipeEntity);
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setLocation(URI.create(recipeResponse.href()));
    return ResponseEntity.ok().headers(responseHeaders).body(recipeResponse);
  }

  @NonNull
  private ResponseRecipe createRecipeResponse(Recipe recipeEntity) {
    return new ResponseRecipe(recipeEntity.getId(), getRecipeHref(recipeEntity.getId()), recipeEntity.getName(),
        recipeEntity.getDescription(), recipeEntity.getCategory(), recipeEntity.getInstructions(),
        recipeEntity.getIngredients(), recipeEntity.getDate(),
        userService.getUserLink(recipeEntity.getCreatedBy().getId()).get(),
        tagService.getTagsLinks(recipeEntity.getTags()),String.valueOf(recipeEntity.getTotalRating()));
  }

  public boolean existsById(long id) {
    return recipeRepository.existsById(id);
  }

  public ResponseEntity<ResponseRecipe> saveRecipe(RecipeRequest request, String email) {

    Recipe recipe = new Recipe(null, request.name(), request.description(), request.ingredients(),
        request.instructions(), request.category());
    recipe.setDate(LocalDate.now());
    recipe.setCreatedBy(email == null ? null : userService.loadUserByEmail(email));
    validateRecipe(recipe);
    List<Tag> tagList = resolveTagEntities(request.tags());
    recipe.setTags(new HashSet<>(tagList));
    if (recipeRepository.existsByName(recipe.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate Recipe Title");
    }
    var entity = recipeRepository.save(recipe);
    ResponseRecipe recipeResponse = createRecipeResponse(entity);
    return ResponseEntity.created(URI.create(recipeResponse.href())).body(recipeResponse);
  }

  // check if user sent non-existing tag ids.
  private List<Tag> resolveTagEntities(List<TagHref> requested) {
    if (requested == null || requested.isEmpty()) {
      return List.of();
    }
    List<Long> requestedIds = requested.stream().map(TagHref::id).toList();
    Iterable<Tag> foundTags = tagRepository.findAllById(requestedIds);
    List<Tag> found = new ArrayList<>();
    foundTags.forEach(found::add);

    Set<Long> foundIds = found.stream().map(Tag::getId).collect(Collectors.toSet());
    List<Long> missingIds = requestedIds.stream().filter(id -> !foundIds.contains(id)).toList();
    if (!missingIds.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tags were not found, ids " + missingIds);
    }
    return found;
  }

  private void validateRecipe(Recipe recipe) {
    Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);

    if (!violations.isEmpty()) {
      String validationError = violations.stream().map(v -> v.getPropertyPath() + " " + v.getMessage()).toList()
          .toString();
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, validationError);
    }
  }

  public ResponseEntity<ResponseRecipe> updateRecipe(Long id, RecipeRequest request, String email) {
    Optional<Recipe> exists = recipeRepository.findById(id);
    if (exists.isPresent()) {
      UserAccount user = userService.loadUserByEmail(email);
      if (exists.get().getCreatedBy().getEmail().equals(email)) {
        Recipe recipe = new Recipe(exists.get().getId(), request.name(), request.description(), request.ingredients(),
            request.instructions(), request.category());
        recipe.setDate(LocalDate.now());
        recipe.setCreatedBy(user);
        validateRecipe(recipe);

        List<Tag> tagList = resolveTagEntities(request.tags());
        recipe.setTags(new HashSet<>(tagList));
        Recipe saved = recipeRepository.save(recipe);
        ResponseRecipe recipeResponse = createRecipeResponse(saved);
        return ResponseEntity.ok(recipeResponse);
      } else {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
            "You are not authorized to update this recipe: " + id);
      }
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe " + id + " Not Found !");
    }
  }

  public List<ResponseRecipe> SearchRecipe(String name) {
    if (!name.isEmpty()) {
      return recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name).stream()
          .map(this::createRecipeResponse).toList();
    }
    return new ArrayList<ResponseRecipe>();
  }

  public HttpStatus removeRecipe(Long id, String email) {
    Optional<Recipe> byId = recipeRepository.findById(id);
    if (byId.isPresent()) {
      if (byId.get().getCreatedBy().getEmail().equals(email)) {
        recipeRepository.delete(byId.get());
        return HttpStatus.NO_CONTENT;
      } else {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are Unauthorized to delete this recipe");
      }
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe " + id + " Not Found !");
    }

  }

  @Scheduled(timeUnit = TimeUnit.MINUTES, fixedDelay = 1L)
  public void UpdateRecipeRating() {
    Iterable<Recipe> allRecipes = this.getAllRecipes();
    for (Recipe next : allRecipes) {
      log.info("Starting updating Recipe Rating id {}", next.getId());
      var totalRatingByRecipe = reviewService.getTotalRatingByRecipe(next.getId());
      if (totalRatingByRecipe.numberOfReviews() > 0) {
        next.setTotalRating(totalRatingByRecipe.score());
        recipeRepository.save(next);
      }

    }
  }

  public Iterable<Recipe> getAllRecipes() {
    return recipeRepository.findAll();
  }

  @Autowired
  public void setReviewService(@Lazy ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  public String getRecipeHref(long id) {
    return ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/api/recipe/{id}")
        .buildAndExpand(id)
        .getPath();
  }

  public Page<ResponseRecipe> getRecipesByTagsAndNamePagination(List<Integer> includedTags, List<Integer> excludedTags,
      int offset, int limit, String name) {
    int pageNumber = offset / limit;
    Pageable pageable = PageRequest.of(pageNumber, limit);
    Page<Recipe> recipePage = recipeRepository.findAll(RecipeSpecs.byTagsandName(includedTags, excludedTags, name),
        pageable);
    List<ResponseRecipe> responseRecipes = recipePage.get().map(this::createRecipeResponse).toList();
    return new PageImpl<>(responseRecipes, pageable, recipePage.getTotalElements());
  }

  public Page<ResponseRecipe> SearchRecipe(String name, int offset, int limit) {
    int pageNumber = offset / limit;
    Pageable pageable = PageRequest.of(pageNumber, limit);
    Page<Recipe> recipePage = recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name, pageable);
    List<ResponseRecipe> responseRecipes = recipePage.get().map(this::createRecipeResponse).toList();
    return new PageImpl<>(responseRecipes, pageable, recipePage.getTotalElements());
  }

  public ResponseEntity<ResponseRecipe> partialUpdateRecipe(Long id, JsonNode patch, String email) {

    Recipe recipe = recipeRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(id));

    UserAccount user = userService.loadUserByEmail(email);
    if (!recipe.getCreatedBy().getEmail().equals(email)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
          "You are not authorized to update this recipe: " + id);
    }

    if (!patch.isObject()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Patch body must be a JSON object");
    }

    for (Map.Entry<String, JsonNode> field : patch.properties()) {
      if (IMMUTABLE_RECIPE_FIELDS.contains(field.getKey())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "Field '" + field.getKey() + "' cannot be patched");
      }
    }

    applyPatch(recipe, patch);

    validateRecipe(recipe);

    Recipe saved = recipeRepository.save(recipe);
    return ResponseEntity.ok(createRecipeResponse(saved));
  }

  private void applyPatch(Recipe recipe, JsonNode patch) {
    if (patch.has("name")) {
      recipe.setName(textValue(patch.get("name"), "name"));
    }
    if (patch.has("description")) {
      recipe.setDescription(textValue(patch.get("description"), "description"));
    }
    if (patch.has("category")) {
      recipe.setCategory(textValue(patch.get("category"), "category"));
    }
    if (patch.has("ingredients")) {
      recipe.setIngredients(stringList(patch.get("ingredients"), "ingredients"));
    }
    if (patch.has("instructions")) {
      recipe.setInstructions(stringList(patch.get("instructions"), "instructions"));
    }
    if (patch.has("tags")) {
      recipe.setTags(resolveTags(patch.get("tags")));
    }
  }

  private Set<Tag> resolveTags(JsonNode node) {
    if (!node.isArray()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field 'tags' must be an array");
    }
    Set<Tag> tags = new HashSet<>();
    for (JsonNode element : node) {
      long tagId = element.isNumber() ? element.longValue()
          : element.path("id").asLong(-1);
      if (tagId < 0) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "Field 'tags' must contain tag ids");
      }
      var tagEntity = tagRepository.findById(tagId);
      if (tagEntity.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag not found with id " + tagId);
      } else {
        tags.add(tagEntity.get());
      }

    }
    return tags;
  }
}
