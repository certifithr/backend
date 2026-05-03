package org.certifit.presentation.trainerinvitation.dto;

import org.certifit.db.entity.enums.InvitationStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record InvitationResponse(
        UUID id,
        UUID trainerProfileId,
        String trainerName,
        UUID clientUserId,
        String email,
        InvitationStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime expiresAt
) {}
