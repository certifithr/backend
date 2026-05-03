package org.certifit.presentation.workout.dto;

import jakarta.validation.constraints.NotNull;
import org.certifit.db.entity.enums.AssignmentStatus;

public record UpdateWorkoutAssignmentRequest(@NotNull AssignmentStatus status) {}
