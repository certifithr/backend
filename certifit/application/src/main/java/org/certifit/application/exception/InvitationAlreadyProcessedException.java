package org.certifit.application.exception;

import java.util.UUID;

public class InvitationAlreadyProcessedException extends RuntimeException {
    public InvitationAlreadyProcessedException(UUID id) {
        super("Invitation " + id + " has already been processed");
    }
}
