package org.certifit.presentation.nutrition.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record NutritionPlanResponse(
        UUID id, UUID trainerId, String title, String description,
        Integer targetCalories, Float targetProteinG, Float targetCarbsG, Float targetFatG,
        boolean isTemplate, OffsetDateTime createdAt
) {}
