package org.certifit.presentation.workout.dto;

import java.util.UUID;

public record WorkoutExerciseResponse(
        UUID id, UUID workoutDayId, UUID exerciseId, String exerciseName,
        Integer orderIndex, Integer sets, Integer reps, Integer restSeconds,
        Float weightKg, Integer durationSeconds, String notes
) {}
