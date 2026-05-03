package org.certifit.presentation.trainerinvitation.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RequestTrainerRequest(
        @NotNull UUID trainerProfileId
) {}
