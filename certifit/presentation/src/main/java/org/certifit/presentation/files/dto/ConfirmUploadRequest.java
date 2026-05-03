package org.certifit.presentation.files.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ConfirmUploadRequest(@NotNull UUID fileId) {}
