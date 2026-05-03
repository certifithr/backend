package org.certifit.presentation.files.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PresignUploadRequest(
        @NotBlank String fileName,
        @NotBlank String mimeType,
        @NotNull Long fileSizeBytes
) {}
