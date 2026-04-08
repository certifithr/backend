package org.certifit.presentation.user.dto;

import org.certifit.db.entity.UserRole;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String avatarUrl,
        boolean isActive,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        UserRole role
) {}
