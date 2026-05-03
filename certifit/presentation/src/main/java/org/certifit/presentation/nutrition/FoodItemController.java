package org.certifit.presentation.nutrition;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.certifit.application.config.JwtAuthenticationFilter.UserPrincipal;
import org.certifit.application.nutrition.FoodItemService;
import org.certifit.presentation.nutrition.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Food Items")
@RestController
@RequestMapping("/api/meals/{mealId}/food-items")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class FoodItemController {

    private final FoodItemService foodItemService;
    private final NutritionMapper nutritionMapper;

    @Operation(summary = "List food items in a meal")
    @GetMapping
    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    public ResponseEntity<List<FoodItemResponse>> getFoodItems(@PathVariable UUID mealId) {
        return ResponseEntity.ok(foodItemService.getFoodItemsByMeal(mealId).stream()
                .map(nutritionMapper::toFoodItemResponse).toList());
    }

    @Operation(summary = "Get a food item by ID")
    @GetMapping("/{itemId}")
    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    public ResponseEntity<FoodItemResponse> getFoodItem(@PathVariable UUID mealId, @PathVariable UUID itemId) {
        return ResponseEntity.ok(nutritionMapper.toFoodItemResponse(foodItemService.getFoodItemById(itemId)));
    }

    @Operation(summary = "Add a food item to a meal")
    @PostMapping
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<FoodItemResponse> addFoodItem(
            @PathVariable UUID mealId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateFoodItemRequest request
    ) {
        var item = foodItemService.addFoodItem(mealId, principal.getId(), request.name(),
                request.quantity(), request.unit(), request.calories(),
                request.proteinG(), request.carbsG(), request.fatG());
        return ResponseEntity.status(HttpStatus.CREATED).body(nutritionMapper.toFoodItemResponse(item));
    }

    @Operation(summary = "Update a food item")
    @PutMapping("/{itemId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<FoodItemResponse> updateFoodItem(
            @PathVariable UUID mealId,
            @PathVariable UUID itemId,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UpdateFoodItemRequest request
    ) {
        var item = foodItemService.updateFoodItem(itemId, principal.getId(), request.name(),
                request.quantity(), request.unit(), request.calories(),
                request.proteinG(), request.carbsG(), request.fatG());
        return ResponseEntity.ok(nutritionMapper.toFoodItemResponse(item));
    }

    @Operation(summary = "Delete a food item")
    @DeleteMapping("/{itemId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<Void> deleteFoodItem(
            @PathVariable UUID mealId,
            @PathVariable UUID itemId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        foodItemService.deleteFoodItem(itemId, principal.getId());
        return ResponseEntity.noContent().build();
    }
}
