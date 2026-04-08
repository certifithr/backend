package org.certifit.application.trainerclient.dto;

import org.certifit.db.entity.enums.CollaborationType;
import org.certifit.db.entity.enums.TrainerClientStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TrainerClientDto(
        UUID id,
        UUID trainerId,
        String trainerFirstName,
        String trainerLastName,
        UUID clientId,
        String clientFirstName,
        String clientLastName,
        CollaborationType collaborationType,
        TrainerClientStatus status,
        String note,
        OffsetDateTime createdAt
) {}