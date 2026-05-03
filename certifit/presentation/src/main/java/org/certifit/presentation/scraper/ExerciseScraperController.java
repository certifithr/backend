package org.certifit.presentation.scraper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.certifit.application.scraper.ExerciseScraperService;
import org.certifit.application.scraper.dto.ExerciseDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/scraper")
@SecurityRequirement(name = "bearerAuth")
public class ExerciseScraperController {

    private final ExerciseScraperService scraperService;

    public ExerciseScraperController(ExerciseScraperService scraperService) {
        this.scraperService = scraperService;
    }

    @PostMapping(value = "/exercises/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload exercises ZIP")
    public ResponseEntity<Map<String, Object>> uploadExercises(
            @Parameter(
                    description = "ZIP file with exercises",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestPart("file") MultipartFile file) {

        log.info("POST /api/scraper/exercises/upload — filename: {}, size: {} bytes",
                file.getOriginalFilename(), file.getSize());

        int saved = scraperService.loadFromZip(file);

        return ResponseEntity.ok(Map.of(
                "message", "Import complete",
                "saved", saved
        ));
    }
}
