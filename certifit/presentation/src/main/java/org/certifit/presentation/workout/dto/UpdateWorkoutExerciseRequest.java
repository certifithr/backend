package org.certifit.presentation.workout.dto;

public record UpdateWorkoutExerciseRequest(
        Integer orderIndex, Integer sets, Integer reps, Integer restSeconds,
        Float weightKg, Integer durationSeconds, String notes
) {}
