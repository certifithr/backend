package org.certifit.presentation.workout.dto;

import jakarta.validation.constraints.NotNull;

public record CreateWorkoutDayRequest(@NotNull Integer dayNumber, String label) {}
