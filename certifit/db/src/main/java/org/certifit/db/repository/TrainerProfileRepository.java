package org.certifit.db.repository;

import org.certifit.db.entity.TrainerProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrainerProfileRepository extends JpaRepository<TrainerProfileEntity, UUID> {
    Optional<TrainerProfileEntity> findByUserId(UUID userId);
    List<TrainerProfileEntity> findAllByIsVerified(boolean isVerified);
}
