package org.certifit.application.exception;

import java.util.UUID;

public class ClientAlreadyAssignedException extends RuntimeException {
    public ClientAlreadyAssignedException(UUID userId) {
        super("Client " + userId + " is already assigned to a trainer");
    }
}
