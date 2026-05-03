package org.certifit.application.exception;

import java.util.UUID;

public class InvitationNotFoundException extends RuntimeException {
    public InvitationNotFoundException(UUID id) {
        super("Invitation not found with ID: " + id);
    }
}
