package org.certifit.presentation.progress.dto;

public record CreateBodyMeasurementRequest(
        Float weightKg,
        Float bodyFatPct,
        Float chestCm,
        Float waistCm,
        Float hipsCm,
        Float thighCm,
        Float armCm
) {}
