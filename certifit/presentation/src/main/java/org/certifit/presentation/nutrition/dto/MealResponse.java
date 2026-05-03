package org.certifit.presentation.nutrition.dto;

import org.certifit.db.entity.enums.MealType;

import java.util.UUID;

public record MealResponse(UUID id, UUID nutritionPlanId, String name, MealType mealType, Integer orderIndex) {}
