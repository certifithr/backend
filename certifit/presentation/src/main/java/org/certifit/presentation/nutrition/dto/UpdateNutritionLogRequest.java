package org.certifit.presentation.nutrition.dto;

public record UpdateNutritionLogRequest(
        Integer caloriesConsumed, Float proteinG, Float carbsG, Float fatG, String notes
) {}
