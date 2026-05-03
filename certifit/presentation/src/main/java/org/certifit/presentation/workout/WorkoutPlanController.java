package org.certifit.presentation.workout;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.certifit.application.config.JwtAuthenticationFilter.UserPrincipal;
import org.certifit.application.workout.WorkoutPlanService;
import org.certifit.presentation.workout.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Workout Plans")
@RestController
@RequestMapping("/api/workout-plans")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;
    private final WorkoutMapper workoutMapper;

    @Operation(summary = "List workout plans for authenticated trainer")
    @GetMapping
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<List<WorkoutPlanResponse>> getMyWorkoutPlans(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(workoutPlanService.getWorkoutPlansByTrainer(principal.getId())
                .stream().map(workoutMapper::toWorkoutPlanResponse).toList());
    }

    @Operation(summary = "Get workout plan by ID")
    @GetMapping("/{planId}")
    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    public ResponseEntity<WorkoutPlanResponse> getWorkoutPlanById(@PathVariable UUID planId) {
        return ResponseEntity.ok(workoutMapper.toWorkoutPlanResponse(workoutPlanService.getWorkoutPlanById(planId)));
    }

    @Operation(summary = "Create a workout plan")
    @PostMapping
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<WorkoutPlanResponse> createWorkoutPlan(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateWorkoutPlanRequest request
    ) {
        var plan = workoutPlanService.createWorkoutPlan(
                principal.getId(), request.title(), request.description(),
                request.difficulty(), request.durationWeeks(), request.isTemplate());
        return ResponseEntity.status(HttpStatus.CREATED).body(workoutMapper.toWorkoutPlanResponse(plan));
    }

    @Operation(summary = "Update a workout plan")
    @PutMapping("/{planId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<WorkoutPlanResponse> updateWorkoutPlan(
            @PathVariable UUID planId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateWorkoutPlanRequest request
    ) {
        var plan = workoutPlanService.updateWorkoutPlan(
                planId, principal.getId(), request.title(), request.description(),
                request.difficulty(), request.durationWeeks(), request.isTemplate());
        return ResponseEntity.ok(workoutMapper.toWorkoutPlanResponse(plan));
    }

    @Operation(summary = "Delete a workout plan (soft delete)")
    @DeleteMapping("/{planId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<Void> deleteWorkoutPlan(
            @PathVariable UUID planId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        workoutPlanService.deleteWorkoutPlan(planId, principal.getId());
        return ResponseEntity.noContent().build();
    }
}
