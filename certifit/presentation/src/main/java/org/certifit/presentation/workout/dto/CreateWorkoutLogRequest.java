package org.certifit.presentation.workout.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record CreateWorkoutLogRequest(
        UUID assignmentId, @NotNull UUID workoutDayId,
        @NotNull LocalDate loggedDate, String notes
) {}
