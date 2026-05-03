package org.certifit.presentation.exerciseform.dto;

import org.certifit.db.entity.enums.AttachmentType;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ExerciseThreadAttachmentResponse(
        UUID id,
        UUID messageId,
        AttachmentType type,
        String fileUrl,
        String thumbnailUrl,
        Integer durationSeconds,
        Long fileSizeBytes,
        String mimeType,
        OffsetDateTime uploadedAt
) {}
