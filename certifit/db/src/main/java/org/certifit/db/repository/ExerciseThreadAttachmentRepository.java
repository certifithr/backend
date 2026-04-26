package org.certifit.db.repository;

import org.certifit.db.entity.ExerciseThreadAttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExerciseThreadAttachmentRepository extends JpaRepository<ExerciseThreadAttachmentEntity, UUID> {

    List<ExerciseThreadAttachmentEntity> findByMessageId(UUID messageId);
}
