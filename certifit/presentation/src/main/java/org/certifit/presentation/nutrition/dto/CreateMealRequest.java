package org.certifit.presentation.nutrition.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.certifit.db.entity.enums.MealType;

public record CreateMealRequest(@NotBlank String name, MealType mealType, @NotNull Integer orderIndex) {}
