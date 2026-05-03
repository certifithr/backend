package org.certifit.application.exception;

public class UnauthorizedInvitationAccessException extends RuntimeException {
    public UnauthorizedInvitationAccessException() {
        super("You are not authorized to perform this action on the invitation");
    }
}
