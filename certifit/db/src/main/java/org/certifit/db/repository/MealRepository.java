package org.certifit.db.repository;

import org.certifit.db.entity.MealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MealRepository extends JpaRepository<MealEntity, UUID> {

    List<MealEntity> findByNutritionPlanIdOrderByOrderIndex(UUID nutritionPlanId);
}
