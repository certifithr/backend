package org.certifit.presentation.exerciseform.dto;

import jakarta.validation.constraints.NotNull;
import org.certifit.db.entity.enums.ThreadStatus;

public record UpdateThreadStatusRequest(
        @NotNull ThreadStatus status
) {}
