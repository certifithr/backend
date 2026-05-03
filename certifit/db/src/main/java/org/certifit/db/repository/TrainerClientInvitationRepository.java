package org.certifit.db.repository;

import org.certifit.db.entity.TrainerClientInvitationEntity;
import org.certifit.db.entity.enums.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TrainerClientInvitationRepository extends JpaRepository<TrainerClientInvitationEntity, UUID> {

    List<TrainerClientInvitationEntity> findByTrainer_IdAndStatus(UUID trainerProfileId, InvitationStatus status);

    List<TrainerClientInvitationEntity> findByClientUser_IdAndStatus(UUID clientUserId, InvitationStatus status);

    boolean existsByTrainer_IdAndClientUser_IdAndStatus(UUID trainerProfileId, UUID clientUserId, InvitationStatus status);

    boolean existsByTrainer_IdAndEmailAndStatus(UUID trainerProfileId, String email, InvitationStatus status);

    List<TrainerClientInvitationEntity> findByStatusAndExpiresAtBefore(InvitationStatus status, OffsetDateTime threshold);
}
