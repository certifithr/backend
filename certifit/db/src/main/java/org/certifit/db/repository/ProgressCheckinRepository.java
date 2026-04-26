package org.certifit.db.repository;

import org.certifit.db.entity.ProgressCheckinEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProgressCheckinRepository extends JpaRepository<ProgressCheckinEntity, UUID> {

    List<ProgressCheckinEntity> findByClientIdOrderByCheckinDateDesc(UUID clientId);

    Optional<ProgressCheckinEntity> findByClientIdAndCheckinDate(UUID clientId, LocalDate checkinDate);
}
