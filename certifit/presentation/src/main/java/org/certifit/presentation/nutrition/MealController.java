package org.certifit.presentation.nutrition;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.certifit.application.config.JwtAuthenticationFilter.UserPrincipal;
import org.certifit.application.nutrition.MealService;
import org.certifit.presentation.nutrition.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Meals")
@RestController
@RequestMapping("/api/nutrition-plans/{planId}/meals")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class MealController {

    private final MealService mealService;
    private final NutritionMapper nutritionMapper;

    @Operation(summary = "List meals in a nutrition plan")
    @GetMapping
    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    public ResponseEntity<List<MealResponse>> getMeals(@PathVariable UUID planId) {
        return ResponseEntity.ok(mealService.getMealsByPlan(planId).stream()
                .map(nutritionMapper::toMealResponse).toList());
    }

    @Operation(summary = "Get a meal by ID")
    @GetMapping("/{mealId}")
    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    public ResponseEntity<MealResponse> getMeal(@PathVariable UUID planId, @PathVariable UUID mealId) {
        return ResponseEntity.ok(nutritionMapper.toMealResponse(mealService.getMealById(mealId)));
    }

    @Operation(summary = "Add a meal to a nutrition plan")
    @PostMapping
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<MealResponse> createMeal(
            @PathVariable UUID planId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateMealRequest request
    ) {
        var meal = mealService.createMeal(planId, principal.getId(), request.name(), request.mealType(), request.orderIndex());
        return ResponseEntity.status(HttpStatus.CREATED).body(nutritionMapper.toMealResponse(meal));
    }

    @Operation(summary = "Update a meal")
    @PutMapping("/{mealId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<MealResponse> updateMeal(
            @PathVariable UUID planId,
            @PathVariable UUID mealId,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UpdateMealRequest request
    ) {
        var meal = mealService.updateMeal(mealId, principal.getId(), request.name(), request.mealType(), request.orderIndex());
        return ResponseEntity.ok(nutritionMapper.toMealResponse(meal));
    }

    @Operation(summary = "Delete a meal")
    @DeleteMapping("/{mealId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<Void> deleteMeal(
            @PathVariable UUID planId,
            @PathVariable UUID mealId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        mealService.deleteMeal(mealId, principal.getId());
        return ResponseEntity.noContent().build();
    }
}
