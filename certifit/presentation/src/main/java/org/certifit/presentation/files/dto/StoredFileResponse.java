package org.certifit.presentation.files.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record StoredFileResponse(
        UUID id,
        String key,
        String bucket,
        String mimeType,
        Long fileSizeBytes,
        String publicUrl,
        OffsetDateTime confirmedAt,
        OffsetDateTime createdAt
) {}
