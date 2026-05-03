package org.certifit.presentation.nutrition.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateFoodItemRequest(
        @NotBlank String name, @NotNull Float quantity, @NotBlank String unit,
        Integer calories, Float proteinG, Float carbsG, Float fatG
) {}
