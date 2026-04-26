package org.certifit.db.repository;

import org.certifit.db.entity.WorkoutExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExerciseEntity, UUID> {

    List<WorkoutExerciseEntity> findByWorkoutDayIdOrderByOrderIndex(UUID workoutDayId);
}
