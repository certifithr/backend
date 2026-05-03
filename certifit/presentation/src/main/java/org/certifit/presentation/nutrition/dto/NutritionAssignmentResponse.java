package org.certifit.presentation.nutrition.dto;

import org.certifit.db.entity.enums.AssignmentStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record NutritionAssignmentResponse(
        UUID id, UUID nutritionPlanId, String nutritionPlanTitle,
        UUID clientProfileId, UUID clientUserId,
        LocalDate startDate, LocalDate endDate,
        AssignmentStatus status, OffsetDateTime assignedAt
) {}
