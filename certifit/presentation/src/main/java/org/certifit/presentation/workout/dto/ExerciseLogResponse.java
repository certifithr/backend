package org.certifit.presentation.workout.dto;
import java.util.UUID;
public record ExerciseLogResponse(
    UUID id, UUID workoutLogId, UUID workoutExerciseId,
    Integer setNumber, Integer repsDone, Float weightUsed,
    Integer durationSeconds, boolean completed
) {}
