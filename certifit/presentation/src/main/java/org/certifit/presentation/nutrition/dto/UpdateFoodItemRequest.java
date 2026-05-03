package org.certifit.presentation.nutrition.dto;

public record UpdateFoodItemRequest(
        String name, Float quantity, String unit,
        Integer calories, Float proteinG, Float carbsG, Float fatG
) {}
