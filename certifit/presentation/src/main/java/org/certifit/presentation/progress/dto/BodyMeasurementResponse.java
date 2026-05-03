package org.certifit.presentation.progress.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record BodyMeasurementResponse(
        UUID id,
        UUID checkinId,
        Float weightKg,
        Float bodyFatPct,
        Float chestCm,
        Float waistCm,
        Float hipsCm,
        Float thighCm,
        Float armCm,
        OffsetDateTime measuredAt
) {}
