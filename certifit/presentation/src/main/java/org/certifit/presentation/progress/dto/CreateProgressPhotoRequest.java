package org.certifit.presentation.progress.dto;

import jakarta.validation.constraints.NotNull;
import org.certifit.db.entity.enums.PhotoAngle;

import java.util.UUID;

public record CreateProgressPhotoRequest(
        @NotNull PhotoAngle angle,
        @NotNull UUID storedFileId
) {}
