package org.certifit.application.scraper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ExerciseDto(
        @JsonProperty("id") Integer id,
        @JsonProperty("name") String name,
        @JsonProperty("slug") String slug,
        @JsonProperty("category") String category,
        @JsonProperty("difficulty") String difficulty,
        @JsonProperty("force") String force,
        @JsonProperty("mechanic") String mechanic,
        @JsonProperty("muscles_primary") List<String> musclesPrimary,
        @JsonProperty("muscles_secondary") List<String> musclesSecondary,
        @JsonProperty("correct_steps") List<String> correctSteps,
        @JsonProperty("description") String description,
        @JsonProperty("media") ExerciseMediaDto media,
        @JsonProperty("body_map_images") List<BodyMapImageDto> bodyMapImages,
        @JsonProperty("variation_of") Object variationOf,
        @JsonProperty("variations") List<Object> variations
) {}
