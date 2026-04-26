package org.certifit.application.exception;

import java.util.UUID;

public class TrainerProfileNotFoundException extends RuntimeException {
    public TrainerProfileNotFoundException(UUID userId) {
        super("Trainer profile not found for user " + userId);
    }
}
