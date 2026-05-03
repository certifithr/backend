package org.certifit.presentation.nutrition.dto;

import org.certifit.db.entity.enums.MealType;

public record UpdateMealRequest(String name, MealType mealType, Integer orderIndex) {}
