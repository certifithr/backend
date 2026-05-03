package org.certifit.presentation.workout.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record WorkoutLogResponse(
        UUID id, UUID clientUserId, UUID workoutAssignmentId,
        UUID workoutDayId, LocalDate loggedDate, String notes, OffsetDateTime createdAt
) {}
