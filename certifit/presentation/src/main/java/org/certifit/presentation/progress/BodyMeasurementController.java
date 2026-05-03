package org.certifit.presentation.progress;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.certifit.application.config.JwtAuthenticationFilter.UserPrincipal;
import org.certifit.application.progress.BodyMeasurementService;
import org.certifit.presentation.progress.dto.BodyMeasurementResponse;
import org.certifit.presentation.progress.dto.CreateBodyMeasurementRequest;
import org.certifit.presentation.progress.dto.UpdateBodyMeasurementRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Body Measurements")
@RestController
@RequestMapping("/api/progress/checkins/{checkinId}/measurements")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class BodyMeasurementController {

    private final BodyMeasurementService bodyMeasurementService;
    private final ProgressMapper progressMapper;

    @Operation(summary = "List measurements for a check-in")
    @GetMapping
    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    public ResponseEntity<List<BodyMeasurementResponse>> getMeasurements(@PathVariable UUID checkinId) {
        return ResponseEntity.ok(bodyMeasurementService.getMeasurementsByCheckin(checkinId)
                .stream().map(progressMapper::toMeasurementResponse).toList());
    }

    @Operation(summary = "Add a body measurement to a check-in")
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<BodyMeasurementResponse> addMeasurement(
            @PathVariable UUID checkinId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateBodyMeasurementRequest request
    ) {
        var measurement = bodyMeasurementService.addMeasurement(
                checkinId, principal.getId(),
                request.weightKg(), request.bodyFatPct(), request.chestCm(),
                request.waistCm(), request.hipsCm(), request.thighCm(), request.armCm());
        return ResponseEntity.status(HttpStatus.CREATED).body(progressMapper.toMeasurementResponse(measurement));
    }

    @Operation(summary = "Update a body measurement")
    @PutMapping("/{measurementId}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<BodyMeasurementResponse> updateMeasurement(
            @PathVariable UUID checkinId,
            @PathVariable UUID measurementId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateBodyMeasurementRequest request
    ) {
        var measurement = bodyMeasurementService.updateMeasurement(
                measurementId, principal.getId(),
                request.weightKg(), request.bodyFatPct(), request.chestCm(),
                request.waistCm(), request.hipsCm(), request.thighCm(), request.armCm());
        return ResponseEntity.ok(progressMapper.toMeasurementResponse(measurement));
    }

    @Operation(summary = "Delete a body measurement")
    @DeleteMapping("/{measurementId}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> deleteMeasurement(
            @PathVariable UUID checkinId,
            @PathVariable UUID measurementId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        bodyMeasurementService.deleteMeasurement(measurementId, principal.getId());
        return ResponseEntity.noContent().build();
    }
}
