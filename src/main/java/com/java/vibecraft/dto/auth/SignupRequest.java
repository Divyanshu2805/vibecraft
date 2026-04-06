package com.java.vibecraft.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(

        @NotBlank(message = "Username is required")
        @Email(message = "Username must be a valid email")
        String username,

        @NotBlank(message = "Name is required")
        @Size(min = 1, max = 30, message = "Name must be between 1 and 30 characters")
        String name,

        @NotBlank(message = "Password is required")
        @Size(min = 4, message = "Password must be at least 4 characters long")
        String password
) {
}
