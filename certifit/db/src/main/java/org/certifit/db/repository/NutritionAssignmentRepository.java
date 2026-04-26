package org.certifit.db.repository;

import org.certifit.db.entity.NutritionAssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NutritionAssignmentRepository extends JpaRepository<NutritionAssignmentEntity, UUID> {

    List<NutritionAssignmentEntity> findByClientId(UUID clientId);
}
