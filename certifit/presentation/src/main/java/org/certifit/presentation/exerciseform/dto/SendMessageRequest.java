package org.certifit.presentation.exerciseform.dto;

import jakarta.validation.constraints.NotBlank;

public record SendMessageRequest(
        @NotBlank String body
) {}
