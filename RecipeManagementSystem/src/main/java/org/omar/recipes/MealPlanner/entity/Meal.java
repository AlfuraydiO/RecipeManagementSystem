/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package org.omar.recipes.MealPlanner.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.omar.recipes.MealPlanner.entity.enums.MealType;
import org.omar.recipes.recipe.entity.Recipe;


@Entity
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private MealType mealType;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.EAGER)
    @JoinTable(name = "MEAL_RECIPE",
            inverseJoinColumns 
                    = @JoinColumn(name = "RECIPE_ID", referencedColumnName = "ID"),
            joinColumns
                    = @JoinColumn(name = "MEAL_ID", referencedColumnName = "ID"))
    private Set<Recipe> recipes = new HashSet<>();
    
    @ManyToOne()
    @JsonIgnore
    private MealPlan mealPlan;

    public Meal() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }

    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<Recipe> recipes) {
        this.recipes = recipes;
    }
    
     public void addRecipes(Recipe r) {
        this.recipes.add(r);
    }
     
     public void removeRecipes(Recipe r) {
        this.recipes.remove(r);
    }

    public MealPlan getMealPlan() {
        return mealPlan;
    }

    public void setMealPlan(MealPlan mealPlan) {
        this.mealPlan = mealPlan;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.mealType);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Meal other = (Meal) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return this.mealType == other.mealType;
    }
    
    @JsonGetter("recipes")
    public List<Long> getRecipesList() {
       return this.recipes.stream().map(e->e.getId()).toList();
    }

    @Override
    public String toString() {
        return "Meal{" + "id=" + id + ", mealType=" + mealType + ", recipes=" + recipes + '}';
    }
    
    


}
