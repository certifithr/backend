package org.certifit.presentation.nutrition.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record CreateNutritionLogRequest(
        UUID assignmentId, @NotNull LocalDate loggedDate,
        Integer caloriesConsumed, Float proteinG, Float carbsG, Float fatG, String notes
) {}
