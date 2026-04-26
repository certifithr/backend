package org.certifit.db.repository;

import org.certifit.db.entity.ExerciseThreadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExerciseThreadRepository extends JpaRepository<ExerciseThreadEntity, UUID> {

    Optional<ExerciseThreadEntity> findByExerciseIdAndClientId(UUID exerciseId, UUID clientId);

    List<ExerciseThreadEntity> findByClientId(UUID clientId);

    List<ExerciseThreadEntity> findByTrainerId(UUID trainerId);
}
