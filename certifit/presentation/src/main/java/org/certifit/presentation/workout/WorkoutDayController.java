package org.certifit.presentation.workout;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.certifit.application.config.JwtAuthenticationFilter.UserPrincipal;
import org.certifit.application.workout.WorkoutDayService;
import org.certifit.presentation.workout.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Workout Days")
@RestController
@RequestMapping("/api/workout-plans/{planId}/days")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class WorkoutDayController {

    private final WorkoutDayService workoutDayService;
    private final WorkoutMapper workoutMapper;

    @Operation(summary = "List days in a workout plan")
    @GetMapping
    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    public ResponseEntity<List<WorkoutDayResponse>> getDays(@PathVariable UUID planId) {
        return ResponseEntity.ok(workoutDayService.getDaysByPlan(planId).stream()
                .map(workoutMapper::toWorkoutDayResponse).toList());
    }

    @Operation(summary = "Get a workout day by ID")
    @GetMapping("/{dayId}")
    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    public ResponseEntity<WorkoutDayResponse> getDay(@PathVariable UUID planId, @PathVariable UUID dayId) {
        return ResponseEntity.ok(workoutMapper.toWorkoutDayResponse(workoutDayService.getDayById(dayId)));
    }

    @Operation(summary = "Add a day to a workout plan")
    @PostMapping
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<WorkoutDayResponse> createDay(
            @PathVariable UUID planId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateWorkoutDayRequest request
    ) {
        var day = workoutDayService.createDay(planId, principal.getId(), request.dayNumber(), request.label());
        return ResponseEntity.status(HttpStatus.CREATED).body(workoutMapper.toWorkoutDayResponse(day));
    }

    @Operation(summary = "Update a workout day")
    @PutMapping("/{dayId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<WorkoutDayResponse> updateDay(
            @PathVariable UUID planId,
            @PathVariable UUID dayId,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UpdateWorkoutDayRequest request
    ) {
        var day = workoutDayService.updateDay(dayId, principal.getId(), request.dayNumber(), request.label());
        return ResponseEntity.ok(workoutMapper.toWorkoutDayResponse(day));
    }

    @Operation(summary = "Delete a workout day")
    @DeleteMapping("/{dayId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<Void> deleteDay(
            @PathVariable UUID planId,
            @PathVariable UUID dayId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        workoutDayService.deleteDay(dayId, principal.getId());
        return ResponseEntity.noContent().build();
    }
}
