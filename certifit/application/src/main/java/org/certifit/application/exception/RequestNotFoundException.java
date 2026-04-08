package org.certifit.application.exception;

import java.util.UUID;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(UUID requestId) {
        super("Trainer-client request not found with ID: " + requestId);
    }
}
