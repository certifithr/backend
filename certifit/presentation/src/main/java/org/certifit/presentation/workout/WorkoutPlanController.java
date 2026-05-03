package org.certifit.presentation.workout;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.certifit.application.workout.WorkoutPlanService;
import org.certifit.db.entity.WorkoutPlanEntity;
import org.certifit.db.entity.enums.DifficultyLevel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workout-plans")
@RequiredArgsConstructor
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    @GetMapping("/trainer/{trainerId}")
    @PreAuthorize("hasRole('TRAINER') and #trainerId == authentication.principal.id")
    public ResponseEntity<List<WorkoutPlanEntity>> getWorkoutPlansByTrainer(@PathVariable UUID trainerId) {
        return ResponseEntity.ok(workoutPlanService.getWorkoutPlansByTrainer(trainerId));
    }

    @GetMapping("/{planId}")
    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    public ResponseEntity<WorkoutPlanEntity> getWorkoutPlanById(@PathVariable UUID planId) {
        return ResponseEntity.ok(workoutPlanService.getWorkoutPlanById(planId));
    }

    @PostMapping("/trainer/{trainerId}")
    @PreAuthorize("hasRole('TRAINER') and #trainerId == authentication.principal.id")
    public ResponseEntity<WorkoutPlanEntity> createWorkoutPlan(
            @PathVariable UUID trainerId,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) DifficultyLevel difficulty,
            @RequestParam(required = false) Integer durationWeeks,
            @RequestParam(required = false, defaultValue = "false") Boolean isTemplate
    ) {
        return ResponseEntity.ok(workoutPlanService.createWorkoutPlan(trainerId, title, description, difficulty, durationWeeks, isTemplate));
    }

    @PutMapping("/{planId}/trainer/{trainerId}")
    @PreAuthorize("hasRole('TRAINER') and #trainerId == authentication.principal.id")
    public ResponseEntity<WorkoutPlanEntity> updateWorkoutPlan(
            @PathVariable UUID planId,
            @PathVariable UUID trainerId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) DifficultyLevel difficulty,
            @RequestParam(required = false) Integer durationWeeks,
            @RequestParam(required = false) Boolean isTemplate
    ) {
        return ResponseEntity.ok(workoutPlanService.updateWorkoutPlan(planId, trainerId, title, description, difficulty, durationWeeks, isTemplate));
    }

    @DeleteMapping("/{planId}/trainer/{trainerId}")
    @PreAuthorize("hasRole('TRAINER') and #trainerId == authentication.principal.id")
    public ResponseEntity<Void> deleteWorkoutPlan(@PathVariable UUID planId, @PathVariable UUID trainerId) {
        workoutPlanService.deleteWorkoutPlan(planId, trainerId);
        return ResponseEntity.noContent().build();
    }
}
