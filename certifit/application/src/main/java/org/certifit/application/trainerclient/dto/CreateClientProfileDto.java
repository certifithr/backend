package org.certifit.application.trainerclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.UUID;

public record CreateClientProfileDto(
        @JsonProperty("user_id") UUID userId,
        @JsonProperty("date_of_birth") LocalDate dateOfBirth,
        @JsonProperty("gender") String gender,
        @JsonProperty("height_cm") Float heightCm,
        @JsonProperty("goal") String goal
) {}
