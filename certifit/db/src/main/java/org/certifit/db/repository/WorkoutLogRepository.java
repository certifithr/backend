package org.certifit.db.repository;

import org.certifit.db.entity.WorkoutLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface WorkoutLogRepository extends JpaRepository<WorkoutLogEntity, UUID> {

    List<WorkoutLogEntity> findByClientId(UUID clientId);

    List<WorkoutLogEntity> findByClientIdAndLoggedDateBetween(UUID clientId, LocalDate start, LocalDate end);
}
