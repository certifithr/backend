package org.certifit.application.trainerclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ClientProfileDto(
        @JsonProperty("id") UUID id,
        @JsonProperty("user_id") UUID userId,
        @JsonProperty("trainer_id") UUID trainerId,
        @JsonProperty("date_of_birth") LocalDate dateOfBirth,
        @JsonProperty("gender") String gender,
        @JsonProperty("height_cm") Float heightCm,
        @JsonProperty("goal") String goal,
        @JsonProperty("status") String status,
        @JsonProperty("onboarded_at") OffsetDateTime onboardedAt,
        @JsonProperty("created_at") OffsetDateTime createdAt,
        @JsonProperty("updated_at") OffsetDateTime updatedAt
) {}
