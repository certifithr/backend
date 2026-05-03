package org.certifit.application.scraper;

import org.certifit.application.scraper.dto.ExerciseDto;
import org.certifit.application.scraper.dto.MuscleWikiPageDto;
import org.certifit.application.scraper.mapper.ExerciseMapper;
import org.certifit.db.entity.ExerciseEntity;
import org.certifit.db.repository.ExerciseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class ExerciseScraperService {

    private static final Logger log = LoggerFactory.getLogger(ExerciseScraperService.class);

    private final ObjectMapper objectMapper;
    private final ExerciseMapper mapper;
    private final ExerciseRepository exerciseRepository;

    public ExerciseScraperService(ObjectMapper objectMapper,
                                  ExerciseMapper mapper,
                                  ExerciseRepository exerciseRepository) {
        this.objectMapper = objectMapper;
        this.mapper = mapper;
        this.exerciseRepository = exerciseRepository;
    }

    @Transactional
    public int loadFromZip(MultipartFile file) {
        int totalSaved = 0;

        try (ZipInputStream zip = new ZipInputStream(file.getInputStream())) {
            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                if (entry.isDirectory() || !entry.getName().endsWith(".json")) {
                    zip.closeEntry();
                    continue;
                }

                log.info("Processing zip entry: {}", entry.getName());

                MuscleWikiPageDto page = objectMapper.readValue(zip.readAllBytes(), MuscleWikiPageDto.class);

                List<ExerciseEntity> entities = page.results().stream()
                        .map(mapper::toDto)
                        .map(this::toEntity)
                        .collect(Collectors.toList());

                if (!entities.isEmpty()) {
                    totalSaved += saveNewExercises(entities);
                }

                zip.closeEntry();
            }
        } catch (IOException e) {
            log.error("Failed to process ZIP file", e);
            throw new RuntimeException("Could not process ZIP", e);
        }

        log.info("ZIP import complete. Total saved: {}", totalSaved);
        return totalSaved;
    }

    private int saveNewExercises(List<ExerciseEntity> entities) {
        // Fetch all existing externalIds in one query instead of N queries
        Set<Integer> existingExternalIds = exerciseRepository.findAllExternalIds();

        List<ExerciseEntity> newEntities = entities.stream()
                .filter(e -> e.getExternalId() == null || !existingExternalIds.contains(e.getExternalId()))
                .collect(Collectors.toList());

        if (newEntities.isEmpty()) {
            log.info("All exercises already exist, skipping");
            return 0;
        }

        exerciseRepository.saveAll(newEntities);
        log.info("Saved {} new exercises", newEntities.size());
        return newEntities.size();
    }

    private ExerciseEntity toEntity(ExerciseDto dto) {
        return ExerciseEntity.builder()
                .externalId(dto.externalId())
                .name(dto.name())
                .slug(dto.slug())
                .category(dto.category())
                .difficulty(dto.difficulty())
                .force(dto.force())
                .mechanic(dto.mechanic())
                .description(dto.description())
                .musclesPrimary(dto.musclesPrimary())
                .musclesSecondary(dto.musclesSecondary())
                .correctSteps(dto.correctSteps())
                .media(dto.media())
                .bodyMapImages(dto.bodyMapImages())
                .variationOf(dto.variationOf())
                .variations(dto.variations())
                .build();
    }
}