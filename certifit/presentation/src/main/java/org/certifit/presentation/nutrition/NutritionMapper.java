package org.certifit.presentation.nutrition;

import org.certifit.db.entity.*;
import org.certifit.presentation.nutrition.dto.*;
import org.springframework.stereotype.Component;

@Component
public class NutritionMapper {

    public NutritionPlanResponse toNutritionPlanResponse(NutritionPlanEntity entity) {
        return new NutritionPlanResponse(
                entity.getId(),
                entity.getTrainer().getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getTargetCalories(),
                entity.getTargetProteinG(),
                entity.getTargetCarbsG(),
                entity.getTargetFatG(),
                entity.isTemplate(),
                entity.getCreatedAt()
        );
    }

    public MealResponse toMealResponse(MealEntity entity) {
        return new MealResponse(
                entity.getId(),
                entity.getNutritionPlan().getId(),
                entity.getName(),
                entity.getMealType(),
                entity.getOrderIndex()
        );
    }

    public FoodItemResponse toFoodItemResponse(FoodItemEntity entity) {
        return new FoodItemResponse(
                entity.getId(),
                entity.getMeal().getId(),
                entity.getName(),
                entity.getQuantity(),
                entity.getUnit(),
                entity.getCalories(),
                entity.getProteinG(),
                entity.getCarbsG(),
                entity.getFatG()
        );
    }

    public NutritionAssignmentResponse toNutritionAssignmentResponse(NutritionAssignmentEntity entity) {
        return new NutritionAssignmentResponse(
                entity.getId(),
                entity.getNutritionPlan().getId(),
                entity.getNutritionPlan().getTitle(),
                entity.getClient().getId(),
                entity.getClient().getUser().getId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getStatus(),
                entity.getAssignedAt()
        );
    }

    public NutritionLogResponse toNutritionLogResponse(NutritionLogEntity entity) {
        return new NutritionLogResponse(
                entity.getId(),
                entity.getClient().getUser().getId(),
                entity.getNutritionAssignment() != null ? entity.getNutritionAssignment().getId() : null,
                entity.getLoggedDate(),
                entity.getCaloriesConsumed(),
                entity.getProteinG(),
                entity.getCarbsG(),
                entity.getFatG(),
                entity.getNotes(),
                entity.getCreatedAt()
        );
    }
}
