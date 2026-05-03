package org.certifit.presentation.workout;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.certifit.application.config.JwtAuthenticationFilter.UserPrincipal;
import org.certifit.application.workout.WorkoutAssignmentService;
import org.certifit.presentation.workout.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Workout Assignments")
@RestController
@RequestMapping("/api/workout-assignments")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class WorkoutAssignmentController {

    private final WorkoutAssignmentService workoutAssignmentService;
    private final WorkoutMapper workoutMapper;

    @Operation(summary = "List assignments for authenticated client")
    @GetMapping("/my")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<WorkoutAssignmentResponse>> getMyAssignments(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(workoutAssignmentService.getAssignmentsByClient(principal.getId())
                .stream().map(workoutMapper::toWorkoutAssignmentResponse).toList());
    }

    @Operation(summary = "List all assignments managed by authenticated trainer")
    @GetMapping("/trainer")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<List<WorkoutAssignmentResponse>> getTrainerAssignments(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(workoutAssignmentService.getAssignmentsByTrainer(principal.getId())
                .stream().map(workoutMapper::toWorkoutAssignmentResponse).toList());
    }

    @Operation(summary = "Assign a workout plan to a client")
    @PostMapping
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<WorkoutAssignmentResponse> assignPlan(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AssignWorkoutPlanRequest request
    ) {
        var assignment = workoutAssignmentService.assignWorkoutPlan(
                principal.getId(), request.planId(), request.clientUserId(),
                request.startDate(), request.endDate());
        return ResponseEntity.status(HttpStatus.CREATED).body(workoutMapper.toWorkoutAssignmentResponse(assignment));
    }

    @Operation(summary = "Update assignment status")
    @PatchMapping("/{assignmentId}/status")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<WorkoutAssignmentResponse> updateStatus(
            @PathVariable UUID assignmentId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateWorkoutAssignmentRequest request
    ) {
        var assignment = workoutAssignmentService.updateAssignmentStatus(assignmentId, principal.getId(), request.status());
        return ResponseEntity.ok(workoutMapper.toWorkoutAssignmentResponse(assignment));
    }
}
