package org.certifit.presentation.exerciseform.dto;

import org.certifit.db.entity.enums.ThreadStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ExerciseThreadResponse(
        UUID id,
        UUID exerciseId,
        String exerciseName,
        UUID clientId,
        String clientName,
        UUID trainerId,
        String trainerName,
        ThreadStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime resolvedAt
) {}
