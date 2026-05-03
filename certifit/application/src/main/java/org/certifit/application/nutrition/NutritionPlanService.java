package org.certifit.application.nutrition;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.application.exception.UserNotFoundException;
import org.certifit.db.entity.NutritionPlanEntity;
import org.certifit.db.entity.UserEntity;
import org.certifit.db.entity.enums.AssignmentStatus;
import org.certifit.db.entity.enums.UserRole;
import org.certifit.db.repository.NutritionAssignmentRepository;
import org.certifit.db.repository.NutritionPlanRepository;
import org.certifit.db.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NutritionPlanService {

    private final NutritionPlanRepository nutritionPlanRepository;
    private final UserRepository userRepository;
    private final NutritionAssignmentRepository nutritionAssignmentRepository;

    @Transactional(readOnly = true)
    public List<NutritionPlanEntity> getNutritionPlansByTrainer(UUID trainerId) {
        log.debug("Getting nutrition plans for trainer: {}", trainerId);
        validateUserIsTrainer(trainerId);
        return nutritionPlanRepository.findByTrainerIdAndDeletedAtIsNull(trainerId);
    }

    @Transactional(readOnly = true)
    public NutritionPlanEntity getNutritionPlanById(UUID planId) {
        log.debug("Getting nutrition plan by id: {}", planId);
        return nutritionPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Nutrition plan not found: " + planId));
    }

    @Transactional
    public NutritionPlanEntity createNutritionPlan(UUID trainerId, String title, String description, Integer targetCalories, Float targetProteinG, Float targetCarbsG, Float targetFatG, Boolean isTemplate) {
        log.info("Creating nutrition plan for trainer: {}", trainerId);
        validateUserIsTrainer(trainerId);

        UserEntity trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new UserNotFoundException(trainerId));

        NutritionPlanEntity plan = new NutritionPlanEntity();
        plan.setTrainer(trainer);
        plan.setTitle(title);
        plan.setDescription(description);
        plan.setTargetCalories(targetCalories);
        plan.setTargetProteinG(targetProteinG);
        plan.setTargetCarbsG(targetCarbsG);
        plan.setTargetFatG(targetFatG);
        plan.setTemplate(isTemplate != null ? isTemplate : false);

        NutritionPlanEntity saved = nutritionPlanRepository.save(plan);
        log.info("Nutrition plan created: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public NutritionPlanEntity updateNutritionPlan(UUID planId, UUID trainerId, String title, String description, Integer targetCalories, Float targetProteinG, Float targetCarbsG, Float targetFatG, Boolean isTemplate) {
        log.info("Updating nutrition plan: {} for trainer: {}", planId, trainerId);
        validateUserIsTrainer(trainerId);

        NutritionPlanEntity plan = nutritionPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Nutrition plan not found: " + planId));

        if (!plan.getTrainer().getId().equals(trainerId)) {
            throw new IllegalArgumentException("Trainer does not own this nutrition plan");
        }

        if (title != null) plan.setTitle(title);
        if (description != null) plan.setDescription(description);
        if (targetCalories != null) plan.setTargetCalories(targetCalories);
        if (targetProteinG != null) plan.setTargetProteinG(targetProteinG);
        if (targetCarbsG != null) plan.setTargetCarbsG(targetCarbsG);
        if (targetFatG != null) plan.setTargetFatG(targetFatG);
        if (isTemplate != null) plan.setTemplate(isTemplate);

        NutritionPlanEntity saved = nutritionPlanRepository.save(plan);
        log.info("Nutrition plan updated: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public void deleteNutritionPlan(UUID planId, UUID trainerId) {
        log.info("Soft deleting nutrition plan: {} for trainer: {}", planId, trainerId);
        validateUserIsTrainer(trainerId);

        NutritionPlanEntity plan = nutritionPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Nutrition plan not found: " + planId));

        if (!plan.getTrainer().getId().equals(trainerId)) {
            throw new IllegalArgumentException("Trainer does not own this nutrition plan");
        }

        // Check if there are active assignments
        long activeAssignments = nutritionAssignmentRepository.findByNutritionPlanId(planId).stream()
                .filter(assignment -> assignment.getStatus() == AssignmentStatus.ACTIVE)
                .count();

        if (activeAssignments > 0) {
            throw new IllegalArgumentException("Cannot delete nutrition plan with active assignments");
        }

        plan.setDeletedAt(java.time.OffsetDateTime.now());
        nutritionPlanRepository.save(plan);
        log.info("Nutrition plan soft deleted: id={}", planId);
    }

    private void validateUserIsTrainer(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (user.getRole() != UserRole.TRAINER) {
            throw new IllegalArgumentException("User is not a trainer: " + userId);
        }
    }
}
