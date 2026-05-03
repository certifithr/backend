package org.certifit.presentation.workout;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.certifit.application.config.JwtAuthenticationFilter.UserPrincipal;
import org.certifit.application.workout.WorkoutExerciseService;
import org.certifit.presentation.workout.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Workout Exercises")
@RestController
@RequestMapping("/api/workout-days/{dayId}/exercises")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class WorkoutExerciseController {

    private final WorkoutExerciseService workoutExerciseService;
    private final WorkoutMapper workoutMapper;

    @Operation(summary = "List exercises in a workout day")
    @GetMapping
    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    public ResponseEntity<List<WorkoutExerciseResponse>> getExercises(@PathVariable UUID dayId) {
        return ResponseEntity.ok(workoutExerciseService.getExercisesByDay(dayId).stream()
                .map(workoutMapper::toWorkoutExerciseResponse).toList());
    }

    @Operation(summary = "Get a workout exercise slot by ID")
    @GetMapping("/{exerciseSlotId}")
    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    public ResponseEntity<WorkoutExerciseResponse> getExercise(@PathVariable UUID dayId, @PathVariable UUID exerciseSlotId) {
        return ResponseEntity.ok(workoutMapper.toWorkoutExerciseResponse(workoutExerciseService.getExerciseById(exerciseSlotId)));
    }

    @Operation(summary = "Add exercise to a workout day")
    @PostMapping
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<WorkoutExerciseResponse> addExercise(
            @PathVariable UUID dayId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateWorkoutExerciseRequest request
    ) {
        var ex = workoutExerciseService.addExercise(dayId, principal.getId(), request.exerciseId(),
                request.orderIndex(), request.sets(), request.reps(), request.restSeconds(),
                request.weightKg(), request.durationSeconds(), request.notes());
        return ResponseEntity.status(HttpStatus.CREATED).body(workoutMapper.toWorkoutExerciseResponse(ex));
    }

    @Operation(summary = "Update a workout exercise slot")
    @PutMapping("/{exerciseSlotId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<WorkoutExerciseResponse> updateExercise(
            @PathVariable UUID dayId,
            @PathVariable UUID exerciseSlotId,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UpdateWorkoutExerciseRequest request
    ) {
        var ex = workoutExerciseService.updateExercise(exerciseSlotId, principal.getId(),
                request.orderIndex(), request.sets(), request.reps(), request.restSeconds(),
                request.weightKg(), request.durationSeconds(), request.notes());
        return ResponseEntity.ok(workoutMapper.toWorkoutExerciseResponse(ex));
    }

    @Operation(summary = "Remove exercise from a workout day")
    @DeleteMapping("/{exerciseSlotId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<Void> removeExercise(
            @PathVariable UUID dayId,
            @PathVariable UUID exerciseSlotId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        workoutExerciseService.removeExercise(exerciseSlotId, principal.getId());
        return ResponseEntity.noContent().build();
    }
}
