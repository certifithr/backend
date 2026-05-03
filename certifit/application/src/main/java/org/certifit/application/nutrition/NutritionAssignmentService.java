package org.certifit.application.nutrition;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.application.exception.ClientProfileNotFoundException;
import org.certifit.db.entity.ClientProfileEntity;
import org.certifit.db.entity.NutritionAssignmentEntity;
import org.certifit.db.entity.NutritionPlanEntity;
import org.certifit.db.entity.UserEntity;
import org.certifit.db.entity.enums.AssignmentStatus;
import org.certifit.db.entity.enums.UserRole;
import org.certifit.db.repository.ClientProfileRepository;
import org.certifit.db.repository.NutritionAssignmentRepository;
import org.certifit.db.repository.NutritionPlanRepository;
import org.certifit.db.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NutritionAssignmentService {

    private final NutritionAssignmentRepository nutritionAssignmentRepository;
    private final NutritionPlanRepository nutritionPlanRepository;
    private final ClientProfileRepository clientProfileRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<NutritionAssignmentEntity> getAssignmentsByClient(UUID clientId) {
        log.debug("Getting nutrition assignments for client: {}", clientId);
        validateUserIsClient(clientId);
        return nutritionAssignmentRepository.findByClientId(clientId);
    }

    @Transactional(readOnly = true)
    public List<NutritionAssignmentEntity> getAssignmentsByTrainer(UUID trainerId) {
        log.debug("Getting nutrition assignments for trainer: {}", trainerId);
        validateUserIsTrainer(trainerId);
        return nutritionAssignmentRepository.findByTrainerId(trainerId);
    }

    @Transactional
    public NutritionAssignmentEntity assignNutritionPlan(UUID trainerId, UUID planId, UUID clientId, LocalDate startDate, LocalDate endDate) {
        log.info("Assigning nutrition plan {} to client {} by trainer {}", planId, clientId, trainerId);
        validateUserIsTrainer(trainerId);
        validateUserIsClient(clientId);

        NutritionPlanEntity plan = nutritionPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Nutrition plan not found: " + planId));

        if (!plan.getTrainer().getId().equals(trainerId)) {
            throw new IllegalArgumentException("Trainer does not own this nutrition plan");
        }

        ClientProfileEntity clientProfile = clientProfileRepository.findByUserId(clientId)
                .orElseThrow(() -> new ClientProfileNotFoundException(clientId));

        if (!clientProfile.getTrainer().getId().equals(trainerId)) {
            throw new IllegalArgumentException("Client is not assigned to this trainer");
        }

        NutritionAssignmentEntity assignment = new NutritionAssignmentEntity();
        assignment.setNutritionPlan(plan);
        assignment.setClient(clientProfile);
        assignment.setStartDate(startDate);
        assignment.setEndDate(endDate);
        assignment.setStatus(AssignmentStatus.ACTIVE);

        NutritionAssignmentEntity saved = nutritionAssignmentRepository.save(assignment);
        log.info("Nutrition assignment created: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public NutritionAssignmentEntity updateAssignmentStatus(UUID assignmentId, UUID trainerId, AssignmentStatus status) {
        log.info("Updating nutrition assignment {} status to {} by trainer {}", assignmentId, status, trainerId);
        validateUserIsTrainer(trainerId);

        NutritionAssignmentEntity assignment = nutritionAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Nutrition assignment not found: " + assignmentId));

        if (!assignment.getNutritionPlan().getTrainer().getId().equals(trainerId)) {
            throw new IllegalArgumentException("Trainer does not own this assignment");
        }

        assignment.setStatus(status);
        NutritionAssignmentEntity saved = nutritionAssignmentRepository.save(assignment);
        log.info("Nutrition assignment updated: id={}", saved.getId());
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
