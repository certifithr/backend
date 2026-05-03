package org.certifit.application.exception;

public class PendingInvitationAlreadyExistsException extends RuntimeException {
    public PendingInvitationAlreadyExistsException(String detail) {
        super("A pending invitation already exists: " + detail);
    }
}
