package org.certifit.application.scraper.dto.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RawImagesDto(
        @JsonProperty("male") List<RawImageItemDto> male,
        @JsonProperty("female") List<RawImageItemDto> female
) {}
