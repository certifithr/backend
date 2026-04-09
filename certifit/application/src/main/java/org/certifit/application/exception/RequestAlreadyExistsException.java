package org.certifit.application.exception;

import java.util.UUID;

public class RequestAlreadyExistsException extends RuntimeException {
    public RequestAlreadyExistsException(UUID clientId, UUID trainerId) {
        super("Request already exists for client " + clientId + " and trainer " + trainerId);
    }
}
