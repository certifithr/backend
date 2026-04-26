package org.certifit.db.repository;

import org.certifit.db.entity.ProgressPhotoEntity;
import org.certifit.db.entity.enums.PhotoAngle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProgressPhotoRepository extends JpaRepository<ProgressPhotoEntity, UUID> {

    List<ProgressPhotoEntity> findByCheckinId(UUID checkinId);

    Optional<ProgressPhotoEntity> findByCheckinIdAndAngle(UUID checkinId, PhotoAngle angle);
}
