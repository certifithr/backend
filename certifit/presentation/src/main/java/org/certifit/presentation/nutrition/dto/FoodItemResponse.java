package org.certifit.presentation.nutrition.dto;

import java.util.UUID;

public record FoodItemResponse(
        UUID id, UUID mealId, String name, Float quantity, String unit,
        Integer calories, Float proteinG, Float carbsG, Float fatG
) {}
