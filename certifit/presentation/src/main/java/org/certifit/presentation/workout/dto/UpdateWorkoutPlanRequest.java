package org.certifit.presentation.workout.dto;

import org.certifit.db.entity.enums.DifficultyLevel;

public record UpdateWorkoutPlanRequest(
        String title, String description, DifficultyLevel difficulty,
        Integer durationWeeks, Boolean isTemplate
) {}
