package org.certifit.presentation.workout.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record AssignWorkoutPlanRequest(
        @NotNull UUID planId, @NotNull UUID clientUserId,
        @NotNull LocalDate startDate, LocalDate endDate
) {}
