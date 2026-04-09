package org.certifit.application.auth.command;

import org.certifit.db.entity.enums.UserRole;

public record SignupCommand(
        String email,
        String password,
        String firstName,
        String lastName,
        UserRole role
) {}
