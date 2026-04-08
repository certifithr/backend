package org.certifit.application.exception;

import org.certifit.db.entity.enums.TrainerClientRequestStatus;

import java.util.UUID;

public class InvalidRequestStateException extends RuntimeException {
    public InvalidRequestStateException(UUID requestId, TrainerClientRequestStatus currentStatus) {
        super("Request " + requestId + " is in invalid state: " + currentStatus + ". Expected PENDING.");
    }
}
