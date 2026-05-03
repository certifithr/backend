package org.certifit.presentation.exerciseform.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateExerciseThreadRequest(
        @NotNull UUID exerciseId,
        @NotNull UUID clientId
) {}
