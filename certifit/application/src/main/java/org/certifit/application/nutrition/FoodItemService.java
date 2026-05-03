package org.certifit.application.nutrition;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.db.entity.FoodItemEntity;
import org.certifit.db.entity.MealEntity;
import org.certifit.db.entity.NutritionPlanEntity;
import org.certifit.db.entity.enums.UserRole;
import org.certifit.db.repository.FoodItemRepository;
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
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;
    private final MealRepository mealRepository;
    private final NutritionPlanRepository nutritionPlanRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<FoodItemEntity> getFoodItemsByMeal(UUID mealId) {
        log.debug("Getting food items for meal: {}", mealId);
        return foodItemRepository.findByMealId(mealId);
    }

    @Transactional(readOnly = true)
    public FoodItemEntity getFoodItemById(UUID itemId) {
        log.debug("Getting food item by id: {}", itemId);
        return foodItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Food item not found: " + itemId));
    }

    @Transactional
    public FoodItemEntity addFoodItem(UUID mealId, UUID trainerId, String name, Float quantity, String unit,
                                       Integer calories, Float proteinG, Float carbsG, Float fatG) {
        log.info("Adding food item to meal: {} by trainer: {}", mealId, trainerId);

        MealEntity meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new IllegalArgumentException("Meal not found: " + mealId));

        validateTrainerOwnsPlan(meal.getNutritionPlan().getId(), trainerId);

        FoodItemEntity item = new FoodItemEntity();
        item.setMeal(meal);
        item.setName(name);
        item.setQuantity(quantity);
        item.setUnit(unit);
        item.setCalories(calories);
        item.setProteinG(proteinG);
        item.setCarbsG(carbsG);
        item.setFatG(fatG);

        FoodItemEntity saved = foodItemRepository.save(item);
        log.info("Food item added: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public FoodItemEntity updateFoodItem(UUID itemId, UUID trainerId, String name, Float quantity, String unit,
                                          Integer calories, Float proteinG, Float carbsG, Float fatG) {
        log.info("Updating food item: {} by trainer: {}", itemId, trainerId);

        FoodItemEntity item = foodItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Food item not found: " + itemId));

        validateTrainerOwnsPlan(item.getMeal().getNutritionPlan().getId(), trainerId);

        if (name != null) item.setName(name);
        if (quantity != null) item.setQuantity(quantity);
        if (unit != null) item.setUnit(unit);
        if (calories != null) item.setCalories(calories);
        if (proteinG != null) item.setProteinG(proteinG);
        if (carbsG != null) item.setCarbsG(carbsG);
        if (fatG != null) item.setFatG(fatG);

        FoodItemEntity saved = foodItemRepository.save(item);
        log.info("Food item updated: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public void deleteFoodItem(UUID itemId, UUID trainerId) {
        log.info("Deleting food item: {} by trainer: {}", itemId, trainerId);

        FoodItemEntity item = foodItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Food item not found: " + itemId));

        validateTrainerOwnsPlan(item.getMeal().getNutritionPlan().getId(), trainerId);
        foodItemRepository.delete(item);
        log.info("Food item deleted: id={}", itemId);
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
