package org.certifit.application.scraper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ExerciseMediaDto(
        @JsonProperty("male") List<MediaItemDto> male,
        @JsonProperty("female") List<MediaItemDto> female
) {}
