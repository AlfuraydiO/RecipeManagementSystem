package org.omar.recipes.MealPlanner.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
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

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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
    MasterMealPlan masterMealPlan=new MasterMealPlan();

    @OneToMany(mappedBy = "mealPlan", cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Meal> meals = new ArrayList<>();

    public MealPlan() {
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
        return "MealPlan{" + "id=" + id + ", date=" + date + ", masterMealPlan=" + masterMealPlan + ", meals=" + meals +'}';
    }

    @JsonGetter("masterMealPlan")
    public long getMasterMealPlanId() {
        return this.masterMealPlan.getId();
    }

    @JsonSetter("masterMealPlan")
    public void getMasterMealPlanId(long id) {
         this.masterMealPlan.setId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealPlan mealPlan = (MealPlan) o;
        return Objects.equals(id, mealPlan.id) && Objects.equals(date, mealPlan.date) && Objects.equals(masterMealPlan, mealPlan.masterMealPlan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, masterMealPlan);
    }
}
