package org.certifit.db.repository;

import org.certifit.db.entity.NutritionLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface NutritionLogRepository extends JpaRepository<NutritionLogEntity, UUID> {

    List<NutritionLogEntity> findByClientId(UUID clientId);

    List<NutritionLogEntity> findByClientIdAndLoggedDateBetween(UUID clientId, LocalDate start, LocalDate end);
}
