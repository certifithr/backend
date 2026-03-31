package org.certifit.application.scraper.dto.raw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RawImageItemDto(
        @JsonProperty("order") int order,
        @JsonProperty("og_image") String ogImage,
        @JsonProperty("branded_video") String brandedVideo
) {}
