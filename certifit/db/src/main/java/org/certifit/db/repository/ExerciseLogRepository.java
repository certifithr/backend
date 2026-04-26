package org.certifit.db.repository;

import org.certifit.db.entity.ExerciseLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExerciseLogRepository extends JpaRepository<ExerciseLogEntity, UUID> {

    List<ExerciseLogEntity> findByWorkoutLogId(UUID workoutLogId);
}
