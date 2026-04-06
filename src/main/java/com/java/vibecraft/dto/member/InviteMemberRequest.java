package com.java.vibecraft.dto.member;

import com.java.vibecraft.enums.ProjectRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InviteMemberRequest(

        @NotBlank(message = "Username is required")
        @Email(message = "Username must be a valid email")
        String username,

        @NotNull(message = "Role is required")
        ProjectRole role
) {
}
