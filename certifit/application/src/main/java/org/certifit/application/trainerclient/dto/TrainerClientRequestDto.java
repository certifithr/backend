package org.certifit.application.trainerclient.dto;

import org.certifit.db.entity.enums.CollaborationType;
import org.certifit.db.entity.enums.TrainerClientRequestStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TrainerClientRequestDto(
        UUID id,
        UUID clientId,
        String clientFirstName,
        String clientLastName,
        UUID trainerId,
        String trainerFirstName,
        String trainerLastName,
        CollaborationType collaborationType,
        TrainerClientRequestStatus status,
        String message,
        OffsetDateTime createdAt
) {}
