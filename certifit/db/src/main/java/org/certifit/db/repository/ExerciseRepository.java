package org.certifit.db.repository;

import org.certifit.db.entity.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExerciseRepository extends JpaRepository<ExerciseEntity, UUID>,
        JpaSpecificationExecutor<ExerciseEntity> {

    Optional<ExerciseEntity> findByExternalId(Integer externalId);
}