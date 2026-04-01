package org.certifit.presentation.exercise;

import org.certifit.application.exercise.ExerciseService;
import org.certifit.application.exercise.dto.ExerciseFilterDto;
import org.certifit.application.scraper.dto.ExerciseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    /**
     * GET /api/exercises
     * GET /api/exercises?name=curl
     * GET /api/exercises?category=Barbell&difficulty=Beginner
     * GET /api/exercises?muscle=Biceps&mechanic=Isolation
     * GET /api/exercises?page=0&size=20&sort=name,asc
     */
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "name", "category", "difficulty", "force", "mechanic", "slug"
    );

    @GetMapping
    public ResponseEntity<Page<ExerciseDto>> getExercises(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String force,
            @RequestParam(required = false) String mechanic,
            @RequestParam(required = false) String muscle,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        // Validate sort fields
        for (Sort.Order order : pageable.getSort()) {
            if (!ALLOWED_SORT_FIELDS.contains(order.getProperty())) {
                return ResponseEntity.badRequest().build();
            }
        }

        ExerciseFilterDto filter = new ExerciseFilterDto(name, category, difficulty, force, mechanic, muscle);
        return ResponseEntity.ok(exerciseService.findAll(filter, pageable));
    }

    /**
     * GET /api/exercises/123
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDto> getExerciseById(@PathVariable Integer id) {
        return exerciseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}