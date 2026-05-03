package org.certifit.presentation.workout.dto;

import java.util.UUID;

public record WorkoutDayResponse(UUID id, UUID workoutPlanId, Integer dayNumber, String label) {}
