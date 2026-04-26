package org.certifit.application.exception;

import java.util.UUID;

public class ClientProfileNotFoundException extends RuntimeException {
    public ClientProfileNotFoundException(UUID userId) {
        super("Client profile not found for user " + userId);
    }
}
