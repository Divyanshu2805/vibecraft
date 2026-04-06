package com.java.vibecraft.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotBlank(message = "Username is required")
        @Email(message = "Username must be a valid email")
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 4, max = 50, message = "Password must be between 4 and 50 characters")
        String password
) {
}
