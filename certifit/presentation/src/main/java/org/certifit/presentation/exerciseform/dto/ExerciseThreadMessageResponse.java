package org.certifit.presentation.exerciseform.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ExerciseThreadMessageResponse(
        UUID id,
        UUID threadId,
        UUID senderId,
        String senderName,
        String body,
        boolean isRead,
        OffsetDateTime sentAt
) {}
