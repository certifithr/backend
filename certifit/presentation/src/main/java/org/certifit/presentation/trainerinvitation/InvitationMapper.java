package org.certifit.presentation.trainerinvitation;

import org.certifit.db.entity.TrainerClientInvitationEntity;
import org.certifit.presentation.trainerinvitation.dto.InvitationResponse;
import org.springframework.stereotype.Component;

@Component
public class InvitationMapper {

    public InvitationResponse toResponse(TrainerClientInvitationEntity entity) {
        var trainer = entity.getTrainer();
        var trainerUser = trainer.getUser();
        String trainerName = trainerUser.getFirstName() + " " + trainerUser.getLastName();

        return new InvitationResponse(
                entity.getId(),
                trainer.getId(),
                trainerName,
                entity.getClientUser() != null ? entity.getClientUser().getId() : null,
                entity.getEmail(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getExpiresAt()
        );
    }
}
