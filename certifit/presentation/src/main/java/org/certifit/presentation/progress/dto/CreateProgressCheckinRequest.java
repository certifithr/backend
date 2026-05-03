package org.certifit.presentation.progress.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateProgressCheckinRequest(
        @NotNull LocalDate checkinDate,
        String trainerNote,
        String clientNote
) {}
