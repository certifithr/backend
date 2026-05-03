package org.certifit.presentation.workout;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.certifit.application.config.JwtAuthenticationFilter.UserPrincipal;
import org.certifit.application.workout.WorkoutLogService;
import org.certifit.presentation.workout.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Workout Logs")
@RestController
@RequestMapping("/api/workout-logs")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class WorkoutLogController {

    private final WorkoutLogService workoutLogService;
    private final WorkoutMapper workoutMapper;

    @Operation(summary = "List workout logs for authenticated client")
    @GetMapping("/my")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<WorkoutLogResponse>> getMyLogs(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(workoutLogService.getWorkoutLogsByClient(principal.getId())
                .stream().map(workoutMapper::toWorkoutLogResponse).toList());
    }

    @Operation(summary = "Get workout log by ID")
    @GetMapping("/{logId}")
    @PreAuthorize("hasRole('CLIENT') or hasRole('TRAINER')")
    public ResponseEntity<WorkoutLogResponse> getLog(@PathVariable UUID logId) {
        return ResponseEntity.ok(workoutMapper.toWorkoutLogResponse(workoutLogService.getWorkoutLogById(logId)));
    }

    @Operation(summary = "Log a workout session")
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<WorkoutLogResponse> createLog(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateWorkoutLogRequest request
    ) {
        var log = workoutLogService.createWorkoutLog(
                principal.getId(), request.assignmentId(), request.workoutDayId(),
                request.loggedDate(), request.notes());
        return ResponseEntity.status(HttpStatus.CREATED).body(workoutMapper.toWorkoutLogResponse(log));
    }

    @Operation(summary = "Update a workout log")
    @PutMapping("/{logId}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<WorkoutLogResponse> updateLog(
            @PathVariable UUID logId,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UpdateWorkoutLogRequest request
    ) {
        var log = workoutLogService.updateWorkoutLog(logId, principal.getId(), request.notes());
        return ResponseEntity.ok(workoutMapper.toWorkoutLogResponse(log));
    }
}
