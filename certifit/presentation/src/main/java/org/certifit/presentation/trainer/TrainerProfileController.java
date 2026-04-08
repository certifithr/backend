package org.certifit.presentation.trainer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.application.trainer.TrainerProfileService;
import org.certifit.db.entity.TrainerProfileEntity;
import org.certifit.presentation.trainer.dto.TrainerProfileResponse;
import org.certifit.presentation.trainer.dto.UpdateTrainerProfileRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
@Tag(name = "Trainer Profile", description = "Trainer profile management endpoints")
public class TrainerProfileController {

    private final TrainerProfileService trainerProfileService;

    @GetMapping("/{userId}/profile")
    @Operation(summary = "Get trainer profile by user ID")
    public TrainerProfileResponse getProfileByUserId(@PathVariable UUID userId) {
        log.info("GET /api/trainers/{}/profile", userId);
        TrainerProfileEntity profile = trainerProfileService.getByUserId(userId);
        return toResponse(profile);
    }

    @GetMapping("/profiles/{profileId}")
    @Operation(summary = "Get trainer profile by profile ID")
    public TrainerProfileResponse getProfileById(@PathVariable UUID profileId) {
        log.info("GET /api/trainers/profiles/{}", profileId);
        TrainerProfileEntity profile = trainerProfileService.getById(profileId);
        return toResponse(profile);
    }

    @GetMapping("/profiles")
    @Operation(summary = "Get all trainer profiles")
    public List<TrainerProfileResponse> getAllProfiles(@RequestParam(required = false) Boolean verified) {
        log.info("GET /api/trainers/profiles — verified: {}", verified);
        List<TrainerProfileEntity> profiles;

        if (verified != null && verified) {
            profiles = trainerProfileService.getVerifiedProfiles();
        } else {
            profiles = trainerProfileService.getAllProfiles();
        }

        return profiles.stream()
                .map(this::toResponse)
                .toList();
    }

    @PutMapping("/{userId}/profile")
    @Operation(summary = "Update trainer profile")
    public TrainerProfileResponse updateProfile(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateTrainerProfileRequest request) {
        log.info("PUT /api/trainers/{}/profile", userId);
        TrainerProfileEntity profile = trainerProfileService.updateProfile(
                userId,
                request.bio(),
                request.specialization()
        );
        return toResponse(profile);
    }

    @DeleteMapping("/{userId}/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete trainer profile")
    public void deleteProfile(@PathVariable UUID userId) {
        log.info("DELETE /api/trainers/{}/profile", userId);
        trainerProfileService.deleteProfile(userId);
    }

    private TrainerProfileResponse toResponse(TrainerProfileEntity entity) {
        return new TrainerProfileResponse(
                entity.getId(),
                entity.getUser().getId(),
                entity.getUser().getEmail(),
                entity.getUser().getFirstName(),
                entity.getUser().getLastName(),
                entity.getBio(),
                entity.getSpecialization(),
                entity.isVerified(),
                entity.getVerifiedAt()
        );
    }
}
