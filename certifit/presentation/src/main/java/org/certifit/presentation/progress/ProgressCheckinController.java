package org.certifit.presentation.progress;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.certifit.application.config.JwtAuthenticationFilter.UserPrincipal;
import org.certifit.application.progress.ProgressCheckinService;
import org.certifit.presentation.progress.dto.CreateProgressCheckinRequest;
import org.certifit.presentation.progress.dto.ProgressCheckinResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Progress Check-ins")
@RestController
@RequestMapping("/api/progress/checkins")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProgressCheckinController {

    private final ProgressCheckinService progressCheckinService;
    private final ProgressMapper progressMapper;

    @Operation(summary = "List check-ins for the authenticated client")
    @GetMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<ProgressCheckinResponse>> getMyCheckins(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(progressCheckinService.getCheckinsByClient(principal.getId())
                .stream().map(progressMapper::toCheckinResponse).toList());
    }

    @Operation(summary = "Get a check-in by ID")
    @GetMapping("/{checkinId}")
    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    public ResponseEntity<ProgressCheckinResponse> getCheckinById(@PathVariable UUID checkinId) {
        return ResponseEntity.ok(progressMapper.toCheckinResponse(progressCheckinService.getCheckinById(checkinId)));
    }

    @Operation(summary = "Create or update a check-in (upsert by date)")
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ProgressCheckinResponse> createOrUpdateCheckin(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateProgressCheckinRequest request
    ) {
        var checkin = progressCheckinService.createOrUpdateCheckin(
                principal.getId(), request.checkinDate(), request.trainerNote(), request.clientNote());
        return ResponseEntity.status(HttpStatus.CREATED).body(progressMapper.toCheckinResponse(checkin));
    }

    @Operation(summary = "Delete a check-in")
    @DeleteMapping("/{checkinId}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> deleteCheckin(
            @PathVariable UUID checkinId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        progressCheckinService.deleteCheckin(checkinId, principal.getId());
        return ResponseEntity.noContent().build();
    }
}
