package org.certifit.application.trainerclient.dto;

import org.certifit.db.entity.enums.TrainerClientStatus;

public record UpdateTrainerClientDto(
        TrainerClientStatus status,
        String note
) {}
