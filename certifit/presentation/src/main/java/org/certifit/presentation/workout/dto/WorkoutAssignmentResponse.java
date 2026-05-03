package org.certifit.presentation.workout.dto;

import org.certifit.db.entity.enums.AssignmentStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record WorkoutAssignmentResponse(
        UUID id, UUID workoutPlanId, String workoutPlanTitle,
        UUID clientProfileId, UUID clientUserId,
        LocalDate startDate, LocalDate endDate,
        AssignmentStatus status, OffsetDateTime assignedAt
) {}
