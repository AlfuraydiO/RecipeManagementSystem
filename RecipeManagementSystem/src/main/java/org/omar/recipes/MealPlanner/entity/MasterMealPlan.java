/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package org.omar.recipes.MealPlanner.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

import org.omar.recipes.MealPlanner.entity.enums.MealType;
import org.omar.recipes.users.entity.UserAccount;


@Entity
public class MasterMealPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "masterMealPlan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<MealPlan> mealPlans = new ArrayList<>();

    @OneToOne()
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserAccount userAccount;

    private String Notes;

    public MasterMealPlan() {
    }

     
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<MealPlan> getMealPlans() {
        return mealPlans;
    }

    public void setMealPlans(List<MealPlan> mealPlans) {
        this.mealPlans = mealPlans;
    }

    public void addMealPlan(MealPlan mealPlan){
        mealPlans.add(mealPlan);
        mealPlan.setMasterMealPlan(this);
    }
    public void removeMealPlan(MealPlan mealPlan){
        mealPlans.remove(mealPlan);
        mealPlan.setMasterMealPlan(null);
    }
    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String Notes) {
        this.Notes = Notes;
    }

}
