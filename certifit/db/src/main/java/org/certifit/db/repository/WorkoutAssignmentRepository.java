package org.certifit.db.repository;

import org.certifit.db.entity.WorkoutAssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkoutAssignmentRepository extends JpaRepository<WorkoutAssignmentEntity, UUID> {

    List<WorkoutAssignmentEntity> findByClientId(UUID clientId);
}
