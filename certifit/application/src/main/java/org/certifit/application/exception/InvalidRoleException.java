package org.certifit.application.exception;

public class InvalidRoleException extends RuntimeException {
    public InvalidRoleException(String userId, String expectedRole) {
        super("User " + userId + " does not have role " + expectedRole);
    }
}
