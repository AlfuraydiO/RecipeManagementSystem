package org.omar.recipes.startupconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.omar.recipes.rating.controller.RatingService;
import org.omar.recipes.recipe.boundary.requestAndResponseBodies.RecipeRequest;
import org.omar.recipes.recipe.controller.RecipeService;
import org.omar.recipes.recipe.controller.TagRepository;
import org.omar.recipes.recipe.entity.Tag;
import org.omar.recipes.users.boundary.RegistrationRequest;
import org.omar.recipes.users.controller.UserAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class StartUp implements CommandLineRunner {

    public record tags(List<Tag> tags) {

    }

    record Recipes(List<RecipeRequest> recipes) {

    }

    TagRepository tagRepository;
    RecipeService recipeService;
    RatingService ratingService;
    UserAccountService userAccountService;

    public StartUp(TagRepository tagRepository, RecipeService recipeService, RatingService ratingService, UserAccountService userAccountService) {
        this.tagRepository = tagRepository;
        this.recipeService = recipeService;
        this.ratingService = ratingService;
        this.userAccountService = userAccountService;
    }

    @Override
    public void run(String... args) throws Exception {
        Path resourcePath = Paths.get("src\\main\\resources").toAbsolutePath();
        List<Path> pathlist = Files.list(resourcePath).toList();
        //pathlist.stream().forEach(e->System.out.println(e.getFileName().toString()));
        Optional<Path> path = pathlist.stream().filter(e -> e.getFileName().toString().equals("tags.json")).findFirst();
        BufferedReader reader = Files.newBufferedReader(path.get());
        String collected = reader.lines().collect(Collectors.joining("\n"));
        ObjectMapper objectMapper = new ObjectMapper();
        tags tags = objectMapper.readValue(collected, tags.class);
        for (Tag tag : tags.tags) {
            tagRepository.save(tag);
        }

        ResponseEntity<String> saveUser = userAccountService.saveNewUser(new RegistrationRequest("omar@email.com", "21423333", "ROLE_CHEF"));

        path = pathlist.stream().filter(e -> e.getFileName().toString().equals("recipes.json")).findFirst();
        reader = Files.newBufferedReader(path.get());
        collected = reader.lines().collect(Collectors.joining("\n"));
        Recipes recipes = objectMapper.readValue(collected, Recipes.class);
        for (RecipeRequest recipe : recipes.recipes) {
            recipeService.saveRecipe(new RecipeRequest(recipe.name(),
                recipe.description(),
                recipe.category(),
                recipe.ingredients(),
                recipe.directions(),
                recipe.tags()), "omar@email.com");
        }
       

        //MealPlanDay mealPlanDay = new MealPlanDay(DayOfWeek.MONDAY, LocalDate.now(), Map.of(Meal.DINNER, 1l));
    }
}
