package org.certifit.application.scraper;

import org.certifit.application.scraper.dto.ExerciseDto;
import org.certifit.application.scraper.dto.MuscleWikiPageDto;
import org.certifit.application.scraper.mapper.ExerciseMapper;
import org.certifit.db.entity.ExerciseEntity;
import org.certifit.db.repository.ExerciseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

                // ZipInputStream can't be passed directly — read entry bytes first
                byte[] bytes = zip.readAllBytes();

                MuscleWikiPageDto page = objectMapper.readValue(bytes, MuscleWikiPageDto.class);

                List<ExerciseEntity> entities = page.results().stream()
                        .map(mapper::toDto)
                        .map(this::toEntity)
                        .collect(Collectors.toList());

                exerciseRepository.saveAll(entities);
                totalSaved += entities.size();
                log.info("Saved {} exercises from {}", entities.size(), entry.getName());

                zip.closeEntry();
            }
        } catch (IOException e) {
            log.error("Failed to process ZIP file", e);
            throw new RuntimeException("Could not process ZIP", e);
        }

        log.info("ZIP import complete. Total saved: {}", totalSaved);
        return totalSaved;
    }

    public List<ExerciseDto> loadFromFile(MultipartFile file) {
        log.info("Parsing uploaded file: {}, size: {} bytes", file.getOriginalFilename(), file.getSize());

        MuscleWikiPageDto page;
        try {
            page = objectMapper.readValue(file.getInputStream(), MuscleWikiPageDto.class);
        } catch (IOException e) {
            log.error("Failed to parse uploaded file", e);
            throw new RuntimeException("Could not parse exercise JSON", e);
        }

        return loadFromPage(page);
    }

    public List<ExerciseDto> loadFromPage(MuscleWikiPageDto page){
        List<ExerciseDto> dtos = page.results().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        List<ExerciseEntity> entities = dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());

        exerciseRepository.saveAll(entities);
        log.info("Saved {} exercises to database", entities.size());

        return dtos;
    }

    private ExerciseEntity toEntity(ExerciseDto dto) {
        return ExerciseEntity.builder()
                .id(dto.id())
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