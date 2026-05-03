package org.certifit.presentation.nutrition.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record NutritionLogResponse(
        UUID id, UUID clientUserId, UUID nutritionAssignmentId,
        LocalDate loggedDate, Integer caloriesConsumed,
        Float proteinG, Float carbsG, Float fatG,
        String notes, OffsetDateTime createdAt
) {}
