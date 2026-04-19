package com.java.vibecraft.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid username address")
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 8)
        String password
) {
}
