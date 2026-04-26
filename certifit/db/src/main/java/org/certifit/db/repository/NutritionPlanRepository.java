package org.certifit.db.repository;

import org.certifit.db.entity.NutritionPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NutritionPlanRepository extends JpaRepository<NutritionPlanEntity, UUID> {

    List<NutritionPlanEntity> findByTrainerId(UUID trainerId);
}
