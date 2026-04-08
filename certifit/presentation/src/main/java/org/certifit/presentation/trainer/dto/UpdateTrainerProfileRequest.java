package org.certifit.presentation.trainer.dto;

import jakarta.validation.constraints.Size;

public record UpdateTrainerProfileRequest(
        @Size(max = 5000, message = "Bio cannot exceed 5000 characters")
        String bio,

        @Size(max = 255, message = "Specialization cannot exceed 255 characters")
        String specialization
) {}
