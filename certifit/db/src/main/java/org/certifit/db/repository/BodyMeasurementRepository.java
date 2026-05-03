package org.certifit.db.repository;

import org.certifit.db.entity.BodyMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BodyMeasurementRepository extends JpaRepository<BodyMeasurementEntity, UUID> {

    List<BodyMeasurementEntity> findByCheckinId(UUID checkinId);
}
