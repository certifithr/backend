package org.certifit.db.repository;

import org.certifit.db.entity.NutritionAssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NutritionAssignmentRepository extends JpaRepository<NutritionAssignmentEntity, UUID> {

    List<NutritionAssignmentEntity> findByClientId(UUID clientId);

    List<NutritionAssignmentEntity> findByNutritionPlanId(UUID nutritionPlanId);

    @Query("SELECT na FROM NutritionAssignmentEntity na WHERE na.nutritionPlan.trainer.id = :trainerId")
    List<NutritionAssignmentEntity> findByTrainerId(UUID trainerId);
}
