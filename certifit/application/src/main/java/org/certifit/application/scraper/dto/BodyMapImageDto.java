package org.certifit.application.scraper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BodyMapImageDto(
        @JsonProperty("gender") String gender,
        @JsonProperty("front") String front,
        @JsonProperty("back") String back
) {}
