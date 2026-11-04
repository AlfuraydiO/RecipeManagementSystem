package org.omar.recipemanagementsystem.review.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicUpdate;
import org.omar.recipemanagementsystem.recipe.entity.Recipe;
import org.omar.recipemanagementsystem.users.entity.UserAccount;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Validated
@DynamicUpdate
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"recipe_id", "user_account_id"})})
public class Review implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rating_seq")
    @SequenceGenerator(name = "rating_seq", sequenceName = "rating_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @NotNull
    private UserAccount userAccount;

    @ManyToOne
    @NotNull
    @JsonIgnore
    private Recipe recipe;

    @NotNull
    private int rating;

    private LocalDateTime localDateTime;

    private String review;


    public Review() {
    }

    public Review(UserAccount userAccount, Recipe recipe, int rating, LocalDateTime localDateTime, String review) {
        this.userAccount = userAccount;
        this.recipe = recipe;
        this.rating = rating;
        this.localDateTime = localDateTime;
        this.review = review;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int recipeRating) {
        this.rating = recipeRating;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }


    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.userAccount);
        hash = 89 * hash + Objects.hashCode(this.recipe);
        hash = 89 * hash + Objects.hashCode(this.rating);
        hash = 89 * hash + Objects.hashCode(this.localDateTime);
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
        final Review other = (Review) obj;
        if (!Objects.equals(this.userAccount, other.userAccount)) {
            return false;
        }
        if (!Objects.equals(this.recipe, other.recipe)) {
            return false;
        }
        if (!Objects.equals(this.rating, other.rating)) {
            return false;
        }
        return Objects.equals(this.localDateTime, other.localDateTime);
    }

    @Override
    public String toString() {
        return "Review{" + "id=" + id + ", userAccount=" + userAccount + ", recipe=" + recipe + ", recipeRating=" + rating + ", localDateTime=" + localDateTime + ", review=" + review + '}';
    }
    
    
}
