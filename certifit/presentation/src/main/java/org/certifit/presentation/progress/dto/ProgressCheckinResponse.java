package org.certifit.presentation.progress.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ProgressCheckinResponse(
        UUID id,
        UUID clientId,
        LocalDate checkinDate,
        String trainerNote,
        String clientNote,
        OffsetDateTime createdAt
) {}
