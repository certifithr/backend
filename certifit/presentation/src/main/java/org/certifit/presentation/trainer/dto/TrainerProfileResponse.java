package org.certifit.presentation.trainer.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TrainerProfileResponse(
        UUID id,
        UUID userId,
        String email,
        String firstName,
        String lastName,
        String bio,
        String specialization,
        boolean isVerified,
        OffsetDateTime verifiedAt
) {}
