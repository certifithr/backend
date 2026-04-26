package org.certifit.db.repository;

import org.certifit.db.entity.WorkoutPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlanEntity, UUID> {

    List<WorkoutPlanEntity> findByTrainerId(UUID trainerId);
}
