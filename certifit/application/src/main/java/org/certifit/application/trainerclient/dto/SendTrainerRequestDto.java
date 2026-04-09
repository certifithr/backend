package org.certifit.application.trainerclient.dto;

import org.certifit.db.entity.enums.CollaborationType;

import java.util.UUID;

public record SendTrainerRequestDto(
        UUID trainerId,
        CollaborationType collaborationType,
        String message
) {}
