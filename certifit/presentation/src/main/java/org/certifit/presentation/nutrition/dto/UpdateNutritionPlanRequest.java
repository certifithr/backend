package org.certifit.presentation.nutrition.dto;

public record UpdateNutritionPlanRequest(
        String title, String description,
        Integer targetCalories, Float targetProteinG, Float targetCarbsG, Float targetFatG,
        Boolean isTemplate
) {}
