package org.certifit.presentation.scraper;

import org.certifit.application.scraper.ExerciseScraperService;
import org.certifit.application.scraper.dto.ExerciseDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scraper")
public class ExerciseScraperController {

    private final ExerciseScraperService scraperService;

    public ExerciseScraperController(ExerciseScraperService scraperService) {
        this.scraperService = scraperService;
    }

    @PostMapping(value = "/exercises/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadExercises(@RequestParam("file") MultipartFile file) {
        int saved = scraperService.loadFromZip(file);
        return ResponseEntity.ok(Map.of(
                "message", "Import complete",
                "saved", saved
        ));
    }
}
