package org.certifit.presentation.nutrition;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.certifit.application.config.JwtAuthenticationFilter.UserPrincipal;
import org.certifit.application.nutrition.NutritionLogService;
import org.certifit.presentation.nutrition.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Nutrition Logs")
@RestController
@RequestMapping("/api/nutrition-logs")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class NutritionLogController {

    private final NutritionLogService nutritionLogService;
    private final NutritionMapper nutritionMapper;

    @Operation(summary = "List nutrition logs for authenticated client")
    @GetMapping("/my")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<NutritionLogResponse>> getMyLogs(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(nutritionLogService.getNutritionLogsByClient(principal.getId())
                .stream().map(nutritionMapper::toNutritionLogResponse).toList());
    }

    @Operation(summary = "Get nutrition log by ID")
    @GetMapping("/{logId}")
    @PreAuthorize("hasRole('CLIENT') or hasRole('TRAINER')")
    public ResponseEntity<NutritionLogResponse> getLog(@PathVariable UUID logId) {
        return ResponseEntity.ok(nutritionMapper.toNutritionLogResponse(nutritionLogService.getNutritionLogById(logId)));
    }

    @Operation(summary = "Log daily nutrition")
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<NutritionLogResponse> createLog(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateNutritionLogRequest request
    ) {
        var log = nutritionLogService.createNutritionLog(
                principal.getId(), request.assignmentId(), request.loggedDate(),
                request.caloriesConsumed(), request.proteinG(), request.carbsG(),
                request.fatG(), request.notes());
        return ResponseEntity.status(HttpStatus.CREATED).body(nutritionMapper.toNutritionLogResponse(log));
    }

    @Operation(summary = "Update a nutrition log")
    @PutMapping("/{logId}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<NutritionLogResponse> updateLog(
            @PathVariable UUID logId,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UpdateNutritionLogRequest request
    ) {
        var log = nutritionLogService.updateNutritionLog(logId, principal.getId(),
                request.caloriesConsumed(), request.proteinG(), request.carbsG(),
                request.fatG(), request.notes());
        return ResponseEntity.ok(nutritionMapper.toNutritionLogResponse(log));
    }
}
