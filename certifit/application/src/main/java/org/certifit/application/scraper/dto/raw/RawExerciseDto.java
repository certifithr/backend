package org.certifit.application.scraper.dto.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RawExerciseDto(
        @JsonProperty("id") Integer id,
        @JsonProperty("name") String name,
        @JsonProperty("slug") String slug,
        @JsonProperty("category") RawNamedDto category,
        @JsonProperty("difficulty") RawNamedDto difficulty,
        @JsonProperty("force") RawNamedDto force,
        @JsonProperty("mechanic") RawNamedDto mechanic,
        @JsonProperty("muscles_primary") List<RawNamedDto> musclesPrimary,
        @JsonProperty("muscles_secondary") List<RawNamedDto> musclesSecondary,
        @JsonProperty("correct_steps") List<RawStepDto> correctSteps,
        @JsonProperty("description") String description,
        @JsonProperty("images") RawImagesDto images,
        @JsonProperty("body_map_images") List<RawBodyMapDto> bodyMapImages,
        @JsonProperty("variation_of") Object variationOf,
        @JsonProperty("variations") List<Object> variations
) {}
