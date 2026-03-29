package org.certifit.db.repository;

import org.certifit.db.entity.TrainerProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrainerProfileRepository extends JpaRepository<TrainerProfileEntity, UUID> {
    Optional<TrainerProfileEntity> findByUserId(UUID userId);
    List<TrainerProfileEntity> findAllByIsVerified(boolean isVerified);
}
