package org.omar.recipes.MealPlanner.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.omar.recipes.recipe.entity.Recipe;
 
@Entity
public class MealPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private LocalDate date;
    
    @ManyToOne
    @JoinColumn(name = "master_id")
    MasterMealPlan masterMealPlan;
 

    @OneToMany(mappedBy = "mealPlan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Meal> meals = new ArrayList<>();

    private String Notes;

    public MealPlan() {
    }

    public MealPlan(Long id, LocalDate date, String Notes) {
        this.id = id;
        this.date = date;
        this.Notes = Notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
 
    public String getNotes() {
        return Notes;
    }

    public void setNotes(String Notes) {
        this.Notes = Notes;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public void addMeals(Meal meal) {
        meals.add(meal);
        meal.setMealPlan(this);
    }

    public void removeMeals(Meal meal) {
        meals.remove(meal);
        meal.setMealPlan(null);
    }
    
     public boolean containsRecipe(Recipe r) {
         return meals.stream().filter(m->m.getRecipes().contains(r)).count()>0;
    }

    public MasterMealPlan getMasterMealPlan() {
        return masterMealPlan;
    }

    public void setMasterMealPlan(MasterMealPlan masterMealPlan) {
        this.masterMealPlan = masterMealPlan;
    }

    @Override
    public String toString() {
        return "MealPlan{" + "id=" + id + ", date=" + date + ", masterMealPlan=" + masterMealPlan + ", meals=" + meals + ", Notes=" + Notes + '}';
    }
    
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.date);
        hash = 67 * hash + Objects.hashCode(this.masterMealPlan);
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
        final MealPlan other = (MealPlan) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        return Objects.equals(this.masterMealPlan, other.masterMealPlan);
    }
     
     
}
