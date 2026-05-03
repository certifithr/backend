package org.certifit.presentation.progress;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.certifit.application.config.JwtAuthenticationFilter.UserPrincipal;
import org.certifit.application.progress.ProgressPhotoService;
import org.certifit.presentation.progress.dto.CreateProgressPhotoRequest;
import org.certifit.presentation.progress.dto.ProgressPhotoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Progress Photos")
@RestController
@RequestMapping("/api/progress/checkins/{checkinId}/photos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProgressPhotoController {

    private final ProgressPhotoService progressPhotoService;
    private final ProgressMapper progressMapper;

    @Operation(summary = "List photos for a check-in")
    @GetMapping
    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    public ResponseEntity<List<ProgressPhotoResponse>> getPhotos(@PathVariable UUID checkinId) {
        return ResponseEntity.ok(progressPhotoService.getPhotosByCheckin(checkinId)
                .stream().map(progressMapper::toPhotoResponse).toList());
    }

    @Operation(summary = "Add a progress photo to a check-in")
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ProgressPhotoResponse> addPhoto(
            @PathVariable UUID checkinId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateProgressPhotoRequest request
    ) {
        var photo = progressPhotoService.addPhoto(
                checkinId, principal.getId(), request.angle(), request.storedFileId());
        return ResponseEntity.status(HttpStatus.CREATED).body(progressMapper.toPhotoResponse(photo));
    }

    @Operation(summary = "Delete a progress photo")
    @DeleteMapping("/{photoId}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> deletePhoto(
            @PathVariable UUID checkinId,
            @PathVariable UUID photoId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        progressPhotoService.deletePhoto(photoId, principal.getId());
        return ResponseEntity.noContent().build();
    }
}
