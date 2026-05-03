package org.certifit.presentation.nutrition;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.certifit.application.config.JwtAuthenticationFilter.UserPrincipal;
import org.certifit.application.nutrition.NutritionPlanService;
import org.certifit.presentation.nutrition.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Nutrition Plans")
@RestController
@RequestMapping("/api/nutrition-plans")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class NutritionPlanController {

    private final NutritionPlanService nutritionPlanService;
    private final NutritionMapper nutritionMapper;

    @Operation(summary = "List nutrition plans for authenticated trainer")
    @GetMapping
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<List<NutritionPlanResponse>> getMyNutritionPlans(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(nutritionPlanService.getNutritionPlansByTrainer(principal.getId())
                .stream().map(nutritionMapper::toNutritionPlanResponse).toList());
    }

    @Operation(summary = "Get nutrition plan by ID")
    @GetMapping("/{planId}")
    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    public ResponseEntity<NutritionPlanResponse> getNutritionPlanById(@PathVariable UUID planId) {
        return ResponseEntity.ok(nutritionMapper.toNutritionPlanResponse(nutritionPlanService.getNutritionPlanById(planId)));
    }

    @Operation(summary = "Create a nutrition plan")
    @PostMapping
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<NutritionPlanResponse> createNutritionPlan(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateNutritionPlanRequest request
    ) {
        var plan = nutritionPlanService.createNutritionPlan(
                principal.getId(), request.title(), request.description(),
                request.targetCalories(), request.targetProteinG(), request.targetCarbsG(),
                request.targetFatG(), request.isTemplate());
        return ResponseEntity.status(HttpStatus.CREATED).body(nutritionMapper.toNutritionPlanResponse(plan));
    }

    @Operation(summary = "Update a nutrition plan")
    @PutMapping("/{planId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<NutritionPlanResponse> updateNutritionPlan(
            @PathVariable UUID planId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateNutritionPlanRequest request
    ) {
        var plan = nutritionPlanService.updateNutritionPlan(planId, principal.getId(),
                request.title(), request.description(), request.targetCalories(),
                request.targetProteinG(), request.targetCarbsG(), request.targetFatG(), request.isTemplate());
        return ResponseEntity.ok(nutritionMapper.toNutritionPlanResponse(plan));
    }

    @Operation(summary = "Delete a nutrition plan (soft delete)")
    @DeleteMapping("/{planId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<Void> deleteNutritionPlan(
            @PathVariable UUID planId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        nutritionPlanService.deleteNutritionPlan(planId, principal.getId());
        return ResponseEntity.noContent().build();
    }
}
