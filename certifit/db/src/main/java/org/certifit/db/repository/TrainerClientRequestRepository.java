package org.certifit.db.repository;

import org.certifit.db.entity.TrainerClientRequestEntity;
import org.certifit.db.entity.enums.TrainerClientRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrainerClientRequestRepository extends JpaRepository<TrainerClientRequestEntity, UUID> {

    boolean existsByClientIdAndTrainerId(UUID clientId, UUID trainerId);

    List<TrainerClientRequestEntity> findByTrainerIdAndStatus(UUID trainerId, TrainerClientRequestStatus status);

    List<TrainerClientRequestEntity> findByClientId(UUID clientId);
}
