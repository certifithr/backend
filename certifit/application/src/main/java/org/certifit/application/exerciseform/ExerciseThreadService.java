package org.certifit.application.exerciseform;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.application.exception.ClientProfileNotFoundException;
import org.certifit.db.entity.ClientProfileEntity;
import org.certifit.db.entity.ExerciseEntity;
import org.certifit.db.entity.ExerciseThreadEntity;
import org.certifit.db.entity.TrainerProfileEntity;
import org.certifit.db.entity.UserEntity;
import org.certifit.db.entity.enums.ThreadStatus;
import org.certifit.db.entity.enums.UserRole;
import org.certifit.db.repository.ClientProfileRepository;
import org.certifit.db.repository.ExerciseRepository;
import org.certifit.db.repository.ExerciseThreadRepository;
import org.certifit.db.repository.TrainerProfileRepository;
import org.certifit.db.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseThreadService {

    private final ExerciseThreadRepository exerciseThreadRepository;
    private final ExerciseRepository exerciseRepository;
    private final ClientProfileRepository clientProfileRepository;
    private final TrainerProfileRepository trainerProfileRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ExerciseThreadEntity> getThreadsByClient(UUID clientId) {
        log.debug("Getting exercise threads for client: {}", clientId);
        validateUserIsClient(clientId);
        return exerciseThreadRepository.findByClientId(clientId);
    }

    @Transactional(readOnly = true)
    public List<ExerciseThreadEntity> getThreadsByTrainer(UUID trainerId) {
        log.debug("Getting exercise threads for trainer: {}", trainerId);
        validateUserIsTrainer(trainerId);
        return exerciseThreadRepository.findByTrainerId(trainerId);
    }

    @Transactional
    public ExerciseThreadEntity createThread(UUID trainerId, UUID exerciseId, UUID clientId) {
        log.info("Creating exercise thread for exercise {} between trainer {} and client {}", exerciseId, trainerId, clientId);
        validateUserIsTrainer(trainerId);
        validateUserIsClient(clientId);

        ExerciseEntity exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found: " + exerciseId));

        ClientProfileEntity clientProfile = clientProfileRepository.findByUserId(clientId)
                .orElseThrow(() -> new ClientProfileNotFoundException(clientId));

        TrainerProfileEntity trainerProfile = trainerProfileRepository.findByUserId(trainerId)
                .orElseThrow(() -> new IllegalArgumentException("Trainer profile not found: " + trainerId));

        if (!clientProfile.getTrainer().getId().equals(trainerProfile.getId())) {
            throw new IllegalArgumentException("Client is not assigned to this trainer");
        }

        // Check if thread already exists
        exerciseThreadRepository.findByExerciseIdAndClientId(exerciseId, clientId)
                .ifPresent(t -> { throw new IllegalArgumentException("Thread already exists for this exercise and client"); });

        ExerciseThreadEntity thread = new ExerciseThreadEntity();
        thread.setExercise(exercise);
        thread.setClient(clientProfile);
        thread.setTrainer(trainerProfile);
        thread.setStatus(ThreadStatus.OPEN);

        ExerciseThreadEntity saved = exerciseThreadRepository.save(thread);
        log.info("Exercise thread created: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public ExerciseThreadEntity updateThreadStatus(UUID threadId, UUID userId, ThreadStatus status) {
        log.info("Updating exercise thread {} status to {} by user {}", threadId, status, userId);

        ExerciseThreadEntity thread = exerciseThreadRepository.findById(threadId)
                .orElseThrow(() -> new IllegalArgumentException("Exercise thread not found: " + threadId));

        // Allow trainer or client to update
        boolean isTrainer = thread.getTrainer().getUser().getId().equals(userId);
        boolean isClient = thread.getClient().getUser().getId().equals(userId);

        if (!isTrainer && !isClient) {
            throw new IllegalArgumentException("User is not part of this thread");
        }

        thread.setStatus(status);
        if (status == ThreadStatus.RESOLVED) {
            thread.setResolvedAt(OffsetDateTime.now());
        }

        ExerciseThreadEntity saved = exerciseThreadRepository.save(thread);
        log.info("Exercise thread updated: id={}", saved.getId());
        return saved;
    }

    private void validateUserIsTrainer(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        if (user.getRole() != UserRole.TRAINER) {
            throw new IllegalArgumentException("User is not a trainer: " + userId);
        }
    }

    private void validateUserIsClient(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        if (user.getRole() != UserRole.CLIENT) {
            throw new IllegalArgumentException("User is not a client: " + userId);
        }
    }
}
