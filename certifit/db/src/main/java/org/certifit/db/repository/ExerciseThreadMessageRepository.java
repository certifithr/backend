package org.certifit.db.repository;

import org.certifit.db.entity.ExerciseThreadMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExerciseThreadMessageRepository extends JpaRepository<ExerciseThreadMessageEntity, UUID> {

    List<ExerciseThreadMessageEntity> findByThreadIdOrderBySentAt(UUID threadId);
}
