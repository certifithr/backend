package org.certifit.application.trainer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.db.entity.TrainerProfileEntity;
import org.certifit.db.entity.UserEntity;
import org.certifit.db.entity.enums.UserRole;
import org.certifit.db.repository.TrainerProfileRepository;
import org.certifit.db.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerProfileService {

    private final TrainerProfileRepository trainerProfileRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public TrainerProfileEntity getByUserId(UUID userId) {
        log.debug("Getting trainer profile for user: {}", userId);
        validateUserIsTrainer(userId);
        return trainerProfileRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultProfile(userId));
    }

    @Transactional(readOnly = true)
    public TrainerProfileEntity getById(UUID profileId) {
        log.debug("Getting trainer profile by id: {}", profileId);
        return trainerProfileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("Trainer profile not found with id: " + profileId));
    }

    @Transactional(readOnly = true)
    public List<TrainerProfileEntity> getAllProfiles() {
        log.debug("Getting all trainer profiles");
        return trainerProfileRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<TrainerProfileEntity> getVerifiedProfiles() {
        log.debug("Getting verified trainer profiles");
        return trainerProfileRepository.findAllByIsVerified(true);
    }

    @Transactional
    public TrainerProfileEntity updateProfile(UUID userId, String bio, String specialization) {
        log.info("Updating trainer profile for user: {}", userId);
        validateUserIsTrainer(userId);

        TrainerProfileEntity profile = trainerProfileRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultProfile(userId));

        if (bio != null) {
            profile.setBio(bio);
        }
        if (specialization != null) {
            profile.setSpecialization(specialization);
        }

        TrainerProfileEntity saved = trainerProfileRepository.save(profile);
        log.info("Trainer profile updated: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public void deleteProfile(UUID userId) {
        log.info("Deleting trainer profile for user: {}", userId);
        validateUserIsTrainer(userId);

        TrainerProfileEntity profile = trainerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Trainer profile not found for user: " + userId));

        trainerProfileRepository.delete(profile);
        log.info("Trainer profile deleted: id={}", profile.getId());
    }

    private TrainerProfileEntity createDefaultProfile(UUID userId) {
        log.info("Creating default trainer profile for user: {}", userId);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        TrainerProfileEntity profile = new TrainerProfileEntity();
        profile.setUser(user);
        profile.setBio("");
        profile.setSpecialization("");
        profile.setVerified(false);

        return trainerProfileRepository.save(profile);
    }

    private void validateUserIsTrainer(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        if (user.getRole() != UserRole.TRAINER) {
            throw new IllegalArgumentException("User is not a trainer: " + userId);
        }
    }
}
