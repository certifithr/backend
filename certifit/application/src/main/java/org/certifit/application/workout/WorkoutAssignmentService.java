package org.certifit.application.workout;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.application.exception.ClientProfileNotFoundException;
import org.certifit.db.entity.ClientProfileEntity;
import org.certifit.db.entity.UserEntity;
import org.certifit.db.entity.WorkoutAssignmentEntity;
import org.certifit.db.entity.WorkoutPlanEntity;
import org.certifit.db.entity.enums.AssignmentStatus;
import org.certifit.db.entity.enums.UserRole;
import org.certifit.db.repository.ClientProfileRepository;
import org.certifit.db.repository.UserRepository;
import org.certifit.db.repository.WorkoutAssignmentRepository;
import org.certifit.db.repository.WorkoutPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkoutAssignmentService {

    private final WorkoutAssignmentRepository workoutAssignmentRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final ClientProfileRepository clientProfileRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<WorkoutAssignmentEntity> getAssignmentsByClient(UUID clientId) {
        log.debug("Getting workout assignments for client: {}", clientId);
        validateUserIsClient(clientId);
        return workoutAssignmentRepository.findByClientId(clientId);
    }

    @Transactional(readOnly = true)
    public List<WorkoutAssignmentEntity> getAssignmentsByTrainer(UUID trainerId) {
        log.debug("Getting workout assignments for trainer: {}", trainerId);
        validateUserIsTrainer(trainerId);
        return workoutAssignmentRepository.findByTrainerId(trainerId);
    }

    @Transactional
    public WorkoutAssignmentEntity assignWorkoutPlan(UUID trainerId, UUID planId, UUID clientId, LocalDate startDate, LocalDate endDate) {
        log.info("Assigning workout plan {} to client {} by trainer {}", planId, clientId, trainerId);
        validateUserIsTrainer(trainerId);
        validateUserIsClient(clientId);

        WorkoutPlanEntity plan = workoutPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Workout plan not found: " + planId));

        if (!plan.getTrainer().getId().equals(trainerId)) {
            throw new IllegalArgumentException("Trainer does not own this workout plan");
        }

        ClientProfileEntity clientProfile = clientProfileRepository.findByUserId(clientId)
                .orElseThrow(() -> new ClientProfileNotFoundException(clientId));

        if (!clientProfile.getTrainer().getId().equals(trainerId)) {
            throw new IllegalArgumentException("Client is not assigned to this trainer");
        }

        WorkoutAssignmentEntity assignment = new WorkoutAssignmentEntity();
        assignment.setWorkoutPlan(plan);
        assignment.setClient(clientProfile);
        assignment.setStartDate(startDate);
        assignment.setEndDate(endDate);
        assignment.setStatus(AssignmentStatus.ACTIVE);

        WorkoutAssignmentEntity saved = workoutAssignmentRepository.save(assignment);
        log.info("Workout assignment created: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public WorkoutAssignmentEntity updateAssignmentStatus(UUID assignmentId, UUID trainerId, AssignmentStatus status) {
        log.info("Updating workout assignment {} status to {} by trainer {}", assignmentId, status, trainerId);
        validateUserIsTrainer(trainerId);

        WorkoutAssignmentEntity assignment = workoutAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Workout assignment not found: " + assignmentId));

        if (!assignment.getWorkoutPlan().getTrainer().getId().equals(trainerId)) {
            throw new IllegalArgumentException("Trainer does not own this assignment");
        }

        assignment.setStatus(status);
        WorkoutAssignmentEntity saved = workoutAssignmentRepository.save(assignment);
        log.info("Workout assignment updated: id={}", saved.getId());
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
