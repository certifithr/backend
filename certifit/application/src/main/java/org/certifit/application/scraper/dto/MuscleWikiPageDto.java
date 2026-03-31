package org.certifit.application.scraper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.certifit.application.scraper.dto.raw.RawExerciseDto;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MuscleWikiPageDto(
        @JsonProperty("count") int count,
        @JsonProperty("next") String next,
        @JsonProperty("results") List<RawExerciseDto> results
) {}
