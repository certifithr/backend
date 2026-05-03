package org.certifit.presentation.nutrition.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateNutritionPlanRequest(
        @NotBlank String title, String description,
        Integer targetCalories, Float targetProteinG, Float targetCarbsG, Float targetFatG,
        boolean isTemplate
) {}
