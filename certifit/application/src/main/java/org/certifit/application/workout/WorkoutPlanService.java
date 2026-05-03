package org.certifit.application.workout;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.application.exception.UserNotFoundException;
import org.certifit.db.entity.UserEntity;
import org.certifit.db.entity.WorkoutPlanEntity;
import org.certifit.db.entity.enums.AssignmentStatus;
import org.certifit.db.entity.enums.DifficultyLevel;
import org.certifit.db.entity.enums.UserRole;
import org.certifit.db.repository.UserRepository;
import org.certifit.db.repository.WorkoutAssignmentRepository;
import org.certifit.db.repository.WorkoutPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final UserRepository userRepository;
    private final WorkoutAssignmentRepository workoutAssignmentRepository;

    @Transactional(readOnly = true)
    public List<WorkoutPlanEntity> getWorkoutPlansByTrainer(UUID trainerId) {
        log.debug("Getting workout plans for trainer: {}", trainerId);
        validateUserIsTrainer(trainerId);
        return workoutPlanRepository.findByTrainerIdAndDeletedAtIsNull(trainerId);
    }

    @Transactional(readOnly = true)
    public WorkoutPlanEntity getWorkoutPlanById(UUID planId) {
        log.debug("Getting workout plan by id: {}", planId);
        return workoutPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Workout plan not found: " + planId));
    }

    @Transactional
    public WorkoutPlanEntity createWorkoutPlan(UUID trainerId, String title, String description, DifficultyLevel difficulty, Integer durationWeeks, Boolean isTemplate) {
        log.info("Creating workout plan for trainer: {}", trainerId);
        validateUserIsTrainer(trainerId);

        UserEntity trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new UserNotFoundException(trainerId));

        WorkoutPlanEntity plan = new WorkoutPlanEntity();
        plan.setTrainer(trainer);
        plan.setTitle(title);
        plan.setDescription(description);
        plan.setDifficulty(difficulty);
        plan.setDurationWeeks(durationWeeks);
        plan.setTemplate(isTemplate != null ? isTemplate : false);

        WorkoutPlanEntity saved = workoutPlanRepository.save(plan);
        log.info("Workout plan created: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public WorkoutPlanEntity updateWorkoutPlan(UUID planId, UUID trainerId, String title, String description, DifficultyLevel difficulty, Integer durationWeeks, Boolean isTemplate) {
        log.info("Updating workout plan: {} for trainer: {}", planId, trainerId);
        validateUserIsTrainer(trainerId);

        WorkoutPlanEntity plan = workoutPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Workout plan not found: " + planId));

        if (!plan.getTrainer().getId().equals(trainerId)) {
            throw new IllegalArgumentException("Trainer does not own this workout plan");
        }

        if (title != null) plan.setTitle(title);
        if (description != null) plan.setDescription(description);
        if (difficulty != null) plan.setDifficulty(difficulty);
        if (durationWeeks != null) plan.setDurationWeeks(durationWeeks);
        if (isTemplate != null) plan.setTemplate(isTemplate);

        WorkoutPlanEntity saved = workoutPlanRepository.save(plan);
        log.info("Workout plan updated: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public void deleteWorkoutPlan(UUID planId, UUID trainerId) {
        log.info("Soft deleting workout plan: {} for trainer: {}", planId, trainerId);
        validateUserIsTrainer(trainerId);

        WorkoutPlanEntity plan = workoutPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Workout plan not found: " + planId));

        if (!plan.getTrainer().getId().equals(trainerId)) {
            throw new IllegalArgumentException("Trainer does not own this workout plan");
        }

        // Check if there are active assignments
        long activeAssignments = workoutAssignmentRepository.findByWorkoutPlanId(planId).stream()
                .filter(assignment -> assignment.getStatus() == AssignmentStatus.ACTIVE)
                .count();

        if (activeAssignments > 0) {
            throw new IllegalArgumentException("Cannot delete workout plan with active assignments");
        }

        plan.setDeletedAt(java.time.OffsetDateTime.now());
        workoutPlanRepository.save(plan);
        log.info("Workout plan soft deleted: id={}", planId);
    }

    private void validateUserIsTrainer(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (user.getRole() != UserRole.TRAINER) {
            throw new IllegalArgumentException("User is not a trainer: " + userId);
        }
    }
}
