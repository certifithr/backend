package org.certifit.application.auth.command;

public record SigninCommand(
        String email,
        String password
) {}
