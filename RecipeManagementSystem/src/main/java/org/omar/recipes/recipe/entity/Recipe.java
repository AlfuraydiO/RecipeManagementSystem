package org.omar.recipes.recipe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.DynamicUpdate;
import org.omar.recipes.users.entity.UserAccount;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Validated
@DynamicUpdate
public class Recipe implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @NotBlank(message = "Recipe name needed")
    String name;
    @NotBlank(message = "Recipe description needed")
    String description;
    @Size(min = 1, message = "At least one ingredient needed")
    @NotNull
    @ElementCollection
    List<String> ingredients;
    @Size(min = 1, message = "At least one direction needed")
    @NotNull
    @ElementCollection
    List<String> directions;

    @NotBlank(message = "Recipe name needed")
    String category;
    @Temporal(TemporalType.TIMESTAMP)
    LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    UserAccount user;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    // @Cascade(value = {org.hibernate.annotations.CascadeType.ALL})
    //@ElementCollection
    @JoinTable(name = "RECIPE_TAGS",
            joinColumns
                    = @JoinColumn(name = "RECIPE_ID", referencedColumnName = "ID"),
            inverseJoinColumns
                    = @JoinColumn(name = "TAG_ID", referencedColumnName = "ID"))
    private Set<Tag> tags = new HashSet<>();


    private double totalRating;

    public Recipe(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Recipe() {
    }

    public Recipe(Long id, String name, String description, List<String> ingredients,
                  List<String> directions, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.directions = directions;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getDirections() {
        return directions;
    }

    public void setDirections(List<String> directions) {
        this.directions = directions;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Recipe recipe = (Recipe) o;
        return Objects.equals(name, recipe.name) && Objects.equals(description, recipe.description) && Objects.equals(ingredients, recipe.ingredients) && Objects.equals(directions, recipe.directions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, ingredients, directions);
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }


    public void AddTag(Tag tag) {
        this.tags.add(tag);
        tag.getRecipeSet().add(this);

    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getRecipeSet().remove(this);

    }

    public double getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(double totalRating) {
        this.totalRating = totalRating;
    }

}
