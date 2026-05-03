package org.certifit.presentation.nutrition.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record AssignNutritionPlanRequest(
        @NotNull UUID planId, @NotNull UUID clientUserId,
        @NotNull LocalDate startDate, LocalDate endDate
) {}
