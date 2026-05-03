package org.certifit.application.nutrition;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.db.entity.MealEntity;
import org.certifit.db.entity.NutritionPlanEntity;
import org.certifit.db.entity.enums.MealType;
import org.certifit.db.entity.enums.UserRole;
import org.certifit.db.repository.MealRepository;
import org.certifit.db.repository.NutritionPlanRepository;
import org.certifit.db.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;
    private final NutritionPlanRepository nutritionPlanRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<MealEntity> getMealsByPlan(UUID planId) {
        log.debug("Getting meals for nutrition plan: {}", planId);
        return mealRepository.findByNutritionPlanIdOrderByOrderIndex(planId);
    }

    @Transactional(readOnly = true)
    public MealEntity getMealById(UUID mealId) {
        log.debug("Getting meal by id: {}", mealId);
        return mealRepository.findById(mealId)
                .orElseThrow(() -> new IllegalArgumentException("Meal not found: " + mealId));
    }

    @Transactional
    public MealEntity createMeal(UUID planId, UUID trainerId, String name, MealType mealType, Integer orderIndex) {
        log.info("Creating meal for plan: {} by trainer: {}", planId, trainerId);
        validateTrainerOwnsPlan(planId, trainerId);

        NutritionPlanEntity plan = nutritionPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Nutrition plan not found: " + planId));

        MealEntity meal = new MealEntity();
        meal.setNutritionPlan(plan);
        meal.setName(name);
        meal.setMealType(mealType);
        meal.setOrderIndex(orderIndex);

        MealEntity saved = mealRepository.save(meal);
        log.info("Meal created: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public MealEntity updateMeal(UUID mealId, UUID trainerId, String name, MealType mealType, Integer orderIndex) {
        log.info("Updating meal: {} by trainer: {}", mealId, trainerId);

        MealEntity meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new IllegalArgumentException("Meal not found: " + mealId));

        validateTrainerOwnsPlan(meal.getNutritionPlan().getId(), trainerId);

        if (name != null) meal.setName(name);
        if (mealType != null) meal.setMealType(mealType);
        if (orderIndex != null) meal.setOrderIndex(orderIndex);

        MealEntity saved = mealRepository.save(meal);
        log.info("Meal updated: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public void deleteMeal(UUID mealId, UUID trainerId) {
        log.info("Deleting meal: {} by trainer: {}", mealId, trainerId);

        MealEntity meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new IllegalArgumentException("Meal not found: " + mealId));

        validateTrainerOwnsPlan(meal.getNutritionPlan().getId(), trainerId);
        mealRepository.delete(meal);
        log.info("Meal deleted: id={}", mealId);
    }

    private void validateTrainerOwnsPlan(UUID planId, UUID trainerId) {
        validateUserIsTrainer(trainerId);
        NutritionPlanEntity plan = nutritionPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Nutrition plan not found: " + planId));
        if (!plan.getTrainer().getId().equals(trainerId)) {
            throw new IllegalArgumentException("Trainer does not own this nutrition plan");
        }
    }

    private void validateUserIsTrainer(UUID userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        if (user.getRole() != UserRole.TRAINER) {
            throw new IllegalArgumentException("User is not a trainer: " + userId);
        }
    }
}
