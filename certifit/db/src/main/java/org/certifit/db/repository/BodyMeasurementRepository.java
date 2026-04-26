package org.certifit.db.repository;

import org.certifit.db.entity.BodyMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BodyMeasurementRepository extends JpaRepository<BodyMeasurementEntity, UUID> {

    Optional<BodyMeasurementEntity> findByCheckinId(UUID checkinId);
}
