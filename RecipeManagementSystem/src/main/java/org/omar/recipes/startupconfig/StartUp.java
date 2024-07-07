package org.omar.recipes.startupconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.omar.recipes.recipe.controller.RecipeService;
import org.omar.recipes.recipe.controller.TagRepository;
import org.omar.recipes.recipe.entity.Recipe;
import org.omar.recipes.recipe.entity.Tag;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.omar.recipes.rating.controller.RatingService;
import org.omar.recipes.rating.entity.Rating;
import org.omar.recipes.users.controller.UserAccountRepository;
import org.omar.recipes.users.entity.UserAccount;
import org.springframework.dao.DataIntegrityViolationException;

@Component
public class StartUp implements CommandLineRunner {

    public record tags(List<Tag> tags){}
    record Recipes(List<Recipe> recipes){}

    TagRepository tagRepository;
    RecipeService recipeService;
    RatingService ratingService;
    UserAccountRepository accountRepository;

    public StartUp(TagRepository tagRepository, RecipeService recipeService, RatingService ratingService, UserAccountRepository accountRepository) {
        this.tagRepository = tagRepository;
        this.recipeService = recipeService;
        this.ratingService = ratingService;
        this.accountRepository = accountRepository;
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
        UserAccount account=new UserAccount();
        account.setAuthority("ROLE_CHEF");
        account.setEmail("omar@email.com");
        account.setEnabled(true);
        account.setPassword("21423333");
        account.setAccountNonLocked(true);
        accountRepository.save(account);
        path = pathlist.stream().filter(e -> e.getFileName().toString().equals("recipes.json")).findFirst();
         reader = Files.newBufferedReader(path.get());
         collected = reader.lines().collect(Collectors.joining("\n"));
        Recipes recipes = objectMapper.readValue(collected, Recipes.class);
        for(Recipe recipe:recipes.recipes){
            recipeService.saveRecipe(recipe,"omar@email.com");
        }
        Recipe get = recipes.recipes.get(2);
        Rating rating =new Rating();
        Recipe newr=new Recipe();
        newr.setId(1l);
        rating.setRecipe(newr);
        //rating.setUserAccount(user);
        rating.setRecipeRating(8.5);
       // Optional<Rating> saveRecipe = ratingService.saveRecipe(new , "omar@email.com");
        try {
            rating =new Rating();
        rating.setRecipe(get);
        rating.setRecipeRating(10.0);
        //saveRecipe = ratingService.saveRecipe(rating, "omar@email.com");
        } catch (DataIntegrityViolationException e) {
            System.err.println("Good");
        }
       // saveRecipe.get().setRecipeRating(10.0);
        //ratingService.updateRating(saveRecipe.get().getId(), rating, "omar@email.com");
        

    }
}
