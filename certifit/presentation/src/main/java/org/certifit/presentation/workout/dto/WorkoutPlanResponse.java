package org.certifit.presentation.workout.dto;

import org.certifit.db.entity.enums.DifficultyLevel;

import java.time.OffsetDateTime;
import java.util.UUID;

public record WorkoutPlanResponse(
        UUID id, UUID trainerId, String title, String description,
        DifficultyLevel difficulty, Integer durationWeeks, boolean isTemplate,
        OffsetDateTime createdAt
) {}
