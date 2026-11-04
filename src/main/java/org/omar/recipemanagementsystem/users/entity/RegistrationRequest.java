package org.omar.recipemanagementsystem.users.entity;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record RegistrationRequest(@NotNull String email, @NotNull String password) {
}
