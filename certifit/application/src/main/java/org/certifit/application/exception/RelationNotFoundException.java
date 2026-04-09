package org.certifit.application.exception;

import java.util.UUID;

public class RelationNotFoundException extends RuntimeException {
    public RelationNotFoundException(UUID relationId) {
        super("Trainer-client relation not found with ID: " + relationId);
    }
}
