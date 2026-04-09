package org.certifit.application.scraper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MediaItemDto(
        @JsonProperty("og_image") String ogImage,
        @JsonProperty("branded_video") String brandedVideo
) {}
