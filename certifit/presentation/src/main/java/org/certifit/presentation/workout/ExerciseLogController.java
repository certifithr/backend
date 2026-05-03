package org.certifit.presentation.workout;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.certifit.application.config.JwtAuthenticationFilter.UserPrincipal;
import org.certifit.application.workout.ExerciseLogService;
import org.certifit.presentation.workout.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Exercise Logs")
@RestController
@RequestMapping("/api/workout-logs/{workoutLogId}/exercise-logs")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ExerciseLogController {

    private final ExerciseLogService exerciseLogService;
    private final WorkoutMapper workoutMapper;

    @Operation(summary = "List exercise logs for a workout session")
    @GetMapping
    @PreAuthorize("hasRole('CLIENT') or hasRole('TRAINER')")
    public ResponseEntity<List<ExerciseLogResponse>> getLogs(@PathVariable UUID workoutLogId) {
        return ResponseEntity.ok(exerciseLogService.getLogsByWorkoutLog(workoutLogId)
                .stream().map(workoutMapper::toExerciseLogResponse).toList());
    }

    @Operation(summary = "Log a set for an exercise")
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ExerciseLogResponse> logExercise(
            @PathVariable UUID workoutLogId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateExerciseLogRequest request
    ) {
        var log = exerciseLogService.logExercise(workoutLogId, principal.getId(),
                request.workoutExerciseId(), request.setNumber(), request.repsDone(),
                request.weightUsed(), request.durationSeconds(), request.completed());
        return ResponseEntity.status(HttpStatus.CREATED).body(workoutMapper.toExerciseLogResponse(log));
    }

    @Operation(summary = "Update an exercise log entry")
    @PutMapping("/{logId}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ExerciseLogResponse> updateLog(
            @PathVariable UUID workoutLogId,
            @PathVariable UUID logId,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UpdateExerciseLogRequest request
    ) {
        var log = exerciseLogService.updateExerciseLog(logId, principal.getId(),
                request.repsDone(), request.weightUsed(), request.durationSeconds(), request.completed());
        return ResponseEntity.ok(workoutMapper.toExerciseLogResponse(log));
    }

    @Operation(summary = "Delete an exercise log entry")
    @DeleteMapping("/{logId}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> deleteLog(
            @PathVariable UUID workoutLogId,
            @PathVariable UUID logId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        exerciseLogService.deleteExerciseLog(logId, principal.getId());
        return ResponseEntity.noContent().build();
    }
}
