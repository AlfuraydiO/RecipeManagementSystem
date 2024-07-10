package org.omar.recipes.users.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import java.io.Serializable;

import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserAccount implements Serializable{

    @Id
    @GeneratedValue
    private Integer id;

    @Email(message = "Please Enter a valid Email")
    @Pattern(regexp = ".+@.+\\..+")
    @NotNull
    @NotBlank(message = "Email must be not blank")
    private String email;
    @JsonIgnore
    @Size(min = 8,message = "Password must be at least 8 characters")
    @NotBlank(message = "Password must not be blank")
    @NotNull
    private String password;
    @JsonIgnore
    private String authority;
    @JsonIgnore
    private boolean isAccountNonLocked;
    @JsonIgnore
    private boolean isEnabled;

    public UserAccount() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }


    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }
     @JsonIgnore
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }
     @JsonIgnore
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount user = (UserAccount) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(authority, user.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, authority);
    }

    @Override
    public String toString() {
        return "ChefUser{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", authority='" + authority + '\'' +
                ", isAccountNonLocked=" + isAccountNonLocked +
                ", isEnabled=" + isEnabled +
                '}';
    }

    
}
