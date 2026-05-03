package org.certifit.presentation.exerciseform.dto;

import jakarta.validation.constraints.NotNull;
import org.certifit.db.entity.enums.AttachmentType;

import java.util.UUID;

public record AddAttachmentRequest(
        @NotNull AttachmentType type,
        @NotNull UUID storedFileId,
        UUID thumbnailStoredFileId,
        Integer durationSeconds
) {}
