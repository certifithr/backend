package org.certifit.db.repository;

import org.certifit.db.entity.TrainerClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrainerClientRepository extends JpaRepository<TrainerClientEntity, UUID> {

    List<TrainerClientEntity> findByTrainerId(UUID trainerId);

    List<TrainerClientEntity> findByClientId(UUID clientId);
}
