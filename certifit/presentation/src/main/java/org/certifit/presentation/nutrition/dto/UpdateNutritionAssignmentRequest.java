package org.certifit.presentation.nutrition.dto;

import jakarta.validation.constraints.NotNull;
import org.certifit.db.entity.enums.AssignmentStatus;

public record UpdateNutritionAssignmentRequest(@NotNull AssignmentStatus status) {}
