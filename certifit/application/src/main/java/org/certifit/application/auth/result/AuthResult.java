package org.certifit.application.auth.result;

import org.certifit.db.entity.enums.UserRole;

import java.util.UUID;

public record AuthResult(
        String token,
        UUID userId,
        String email,
        String firstName,
        String lastName,
        UserRole role
) {}