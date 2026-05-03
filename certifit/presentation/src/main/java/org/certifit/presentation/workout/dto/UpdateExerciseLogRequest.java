package org.certifit.presentation.workout.dto;
public record UpdateExerciseLogRequest(
    Integer repsDone, Float weightUsed, Integer durationSeconds, Boolean completed
) {}
