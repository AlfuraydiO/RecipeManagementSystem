package org.omar.recipes.startupconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.omar.recipes.recipe.controller.RecipeRepository;
import org.omar.recipes.recipe.controller.RecipeService;
import org.omar.recipes.recipe.controller.TagRepository;
import org.omar.recipes.recipe.entity.Recipe;
import org.omar.recipes.recipe.entity.Tag;
import org.omar.recipes.users.entity.UserAccount;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class StartUp implements CommandLineRunner {

    public record tags(List<Tag> tags){}
    record Recipes(List<Recipe> recipes){}

    TagRepository tagRepository;
    RecipeService recipeService;

    public StartUp(TagRepository tagRepository, RecipeService recipeService) {
        this.tagRepository = tagRepository;
        this.recipeService = recipeService;
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
        for(Tag tag:tags.tags){
            tagRepository.save(tag);
        }

        path = pathlist.stream().filter(e -> e.getFileName().toString().equals("recipes.json")).findFirst();
         reader = Files.newBufferedReader(path.get());
         collected = reader.lines().collect(Collectors.joining("\n"));
        Recipes recipes = objectMapper.readValue(collected, Recipes.class);
        for(Recipe recipe:recipes.recipes){
            recipeService.saveRecipe(recipe,null);
        }


    }
}
