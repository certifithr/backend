package org.certifit.presentation.workout.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateWorkoutExerciseRequest(
        @NotNull UUID exerciseId, @NotNull Integer orderIndex,
        Integer sets, Integer reps, Integer restSeconds,
        Float weightKg, Integer durationSeconds, String notes
) {}
