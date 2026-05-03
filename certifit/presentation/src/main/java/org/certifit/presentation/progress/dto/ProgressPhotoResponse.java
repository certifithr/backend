package org.certifit.presentation.progress.dto;

import org.certifit.db.entity.enums.PhotoAngle;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ProgressPhotoResponse(
        UUID id,
        UUID checkinId,
        PhotoAngle angle,
        String photoUrl,
        OffsetDateTime takenAt
) {}
