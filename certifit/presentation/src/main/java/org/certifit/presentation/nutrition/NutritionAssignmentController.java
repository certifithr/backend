package org.certifit.presentation.nutrition;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.certifit.application.config.JwtAuthenticationFilter.UserPrincipal;
import org.certifit.application.nutrition.NutritionAssignmentService;
import org.certifit.presentation.nutrition.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Nutrition Assignments")
@RestController
@RequestMapping("/api/nutrition-assignments")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class NutritionAssignmentController {

    private final NutritionAssignmentService nutritionAssignmentService;
    private final NutritionMapper nutritionMapper;

    @Operation(summary = "List assignments for authenticated client")
    @GetMapping("/my")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<NutritionAssignmentResponse>> getMyAssignments(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(nutritionAssignmentService.getAssignmentsByClient(principal.getId())
                .stream().map(nutritionMapper::toNutritionAssignmentResponse).toList());
    }

    @Operation(summary = "List all assignments managed by authenticated trainer")
    @GetMapping("/trainer")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<List<NutritionAssignmentResponse>> getTrainerAssignments(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(nutritionAssignmentService.getAssignmentsByTrainer(principal.getId())
                .stream().map(nutritionMapper::toNutritionAssignmentResponse).toList());
    }

    @Operation(summary = "Assign a nutrition plan to a client")
    @PostMapping
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<NutritionAssignmentResponse> assignPlan(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AssignNutritionPlanRequest request
    ) {
        var assignment = nutritionAssignmentService.assignNutritionPlan(
                principal.getId(), request.planId(), request.clientUserId(),
                request.startDate(), request.endDate());
        return ResponseEntity.status(HttpStatus.CREATED).body(nutritionMapper.toNutritionAssignmentResponse(assignment));
    }

    @Operation(summary = "Update nutrition assignment status")
    @PatchMapping("/{assignmentId}/status")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<NutritionAssignmentResponse> updateStatus(
            @PathVariable UUID assignmentId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateNutritionAssignmentRequest request
    ) {
        var assignment = nutritionAssignmentService.updateAssignmentStatus(assignmentId, principal.getId(), request.status());
        return ResponseEntity.ok(nutritionMapper.toNutritionAssignmentResponse(assignment));
    }
}
