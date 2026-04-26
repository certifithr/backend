package org.certifit.application.scraper.mapper;

import org.certifit.application.scraper.dto.*;
import org.certifit.application.scraper.dto.raw.*;
import org.certifit.db.entity.ExerciseEntity;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class ExerciseMapper {

    private final ObjectMapper objectMapper;

    public ExerciseMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private static final Pattern HTML_TAG = Pattern.compile("<[^>]+>");

    public ExerciseDto toDto(RawExerciseDto raw) {
        return new ExerciseDto(
                UUID.randomUUID(),
                raw.id(),
                raw.name(),
                raw.slug(),
                getName(raw.category()),
                getName(raw.difficulty()),
                getName(raw.force()),
                getName(raw.mechanic()),
                toNameList(raw.musclesPrimary()),
                toNameList(raw.musclesSecondary()),
                toStepList(raw.correctSteps()),
                stripHtml(raw.description()),
                toMediaDto(raw.images()),
                toBodyMapList(raw.bodyMapImages()),
                raw.variationOf(),
                raw.variations() != null ? raw.variations() : Collections.emptyList()
        );
    }

    private String getName(RawNamedDto dto) {
        return dto != null ? dto.name() : null;
    }

    private List<String> toNameList(List<RawNamedDto> list) {
        if (list == null) return Collections.emptyList();
        return list.stream()
                .map(RawNamedDto::name)
                .collect(Collectors.toList());
    }

    private List<String> toStepList(List<RawStepDto> steps) {
        if (steps == null) return Collections.emptyList();
        return steps.stream()
                .sorted(Comparator.comparingInt(RawStepDto::order))
                .map(RawStepDto::text)
                .collect(Collectors.toList());
    }

    private ExerciseMediaDto toMediaDto(RawImagesDto images) {
        if (images == null) return new ExerciseMediaDto(Collections.emptyList(), Collections.emptyList());
        return new ExerciseMediaDto(
                toMediaItemList(images.male()),
                toMediaItemList(images.female())
        );
    }

    private List<MediaItemDto> toMediaItemList(List<RawImageItemDto> items) {
        if (items == null) return Collections.emptyList();
        return items.stream()
                .sorted(Comparator.comparingInt(RawImageItemDto::order))
                .map(i -> new MediaItemDto(i.ogImage(), i.brandedVideo()))
                .collect(Collectors.toList());
    }

    private List<BodyMapImageDto> toBodyMapList(List<RawBodyMapDto> list) {
        if (list == null) return Collections.emptyList();
        return list.stream()
                .map(bm -> new BodyMapImageDto(
                        bm.gender() != null ? bm.gender().name() : null,
                        bm.front(),
                        bm.back()
                ))
                .collect(Collectors.toList());
    }

    private String stripHtml(String html) {
        if (html == null || html.isBlank()) return "";
        return HTML_TAG.matcher(html).replaceAll("").trim();
    }

    public ExerciseDto fromEntity(ExerciseEntity entity) {
        return new ExerciseDto(
                entity.getId(),
                entity.getExternalId(),
                entity.getName(),
                entity.getSlug(),
                entity.getCategory(),
                entity.getDifficulty(),
                entity.getForce(),
                entity.getMechanic(),
                entity.getMusclesPrimary(),
                entity.getMusclesSecondary(),
                entity.getCorrectSteps(),
                entity.getDescription(),
                objectMapper.convertValue(entity.getMedia(), ExerciseMediaDto.class),
                objectMapper.convertValue(
                        entity.getBodyMapImages(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, BodyMapImageDto.class)
                ),
                entity.getVariationOf(),
                entity.getVariations()
        );
    }
}
