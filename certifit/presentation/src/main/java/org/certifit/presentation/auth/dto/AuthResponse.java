package org.certifit.presentation.auth.dto;

import org.certifit.db.entity.enums.UserRole;

import java.util.UUID;

public record AuthResponse(
        String token,
        UUID userId,
        String email,
        String firstName,
        String lastName,
        UserRole role
) {}
