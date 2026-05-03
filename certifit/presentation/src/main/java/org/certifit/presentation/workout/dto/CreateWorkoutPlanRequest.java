package org.certifit.presentation.workout.dto;

import jakarta.validation.constraints.NotBlank;
import org.certifit.db.entity.enums.DifficultyLevel;

public record CreateWorkoutPlanRequest(
        @NotBlank String title, String description,
        DifficultyLevel difficulty, Integer durationWeeks, boolean isTemplate
) {}
