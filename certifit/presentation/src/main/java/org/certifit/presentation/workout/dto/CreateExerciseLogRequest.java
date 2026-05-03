package org.certifit.presentation.workout.dto;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
public record CreateExerciseLogRequest(
    @NotNull UUID workoutExerciseId, @NotNull Integer setNumber,
    Integer repsDone, Float weightUsed, Integer durationSeconds,
    boolean completed
) {}
