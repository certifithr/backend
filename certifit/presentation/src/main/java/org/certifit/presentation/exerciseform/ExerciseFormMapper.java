package org.certifit.presentation.exerciseform;

import lombok.RequiredArgsConstructor;
import org.certifit.db.entity.ExerciseThreadAttachmentEntity;
import org.certifit.db.entity.ExerciseThreadEntity;
import org.certifit.db.entity.ExerciseThreadMessageEntity;
import org.certifit.db.entity.UserEntity;
import org.certifit.presentation.exerciseform.dto.ExerciseThreadAttachmentResponse;
import org.certifit.presentation.exerciseform.dto.ExerciseThreadMessageResponse;
import org.certifit.presentation.exerciseform.dto.ExerciseThreadResponse;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExerciseFormMapper {

    public ExerciseThreadResponse toThreadResponse(ExerciseThreadEntity entity) {
        UserEntity clientUser = entity.getClient().getUser();
        UserEntity trainerUser = entity.getTrainer().getUser();
        return new ExerciseThreadResponse(
                entity.getId(),
                entity.getExercise().getId(),
                entity.getExercise().getName(),
                clientUser.getId(),
                clientUser.getFirstName() + " " + clientUser.getLastName(),
                trainerUser.getId(),
                trainerUser.getFirstName() + " " + trainerUser.getLastName(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getResolvedAt()
        );
    }

    public ExerciseThreadMessageResponse toMessageResponse(ExerciseThreadMessageEntity entity) {
        UserEntity sender = entity.getSender();
        return new ExerciseThreadMessageResponse(
                entity.getId(),
                entity.getThread().getId(),
                sender.getId(),
                sender.getFirstName() + " " + sender.getLastName(),
                entity.getBody(),
                entity.isRead(),
                entity.getSentAt()
        );
    }

    public ExerciseThreadAttachmentResponse toAttachmentResponse(ExerciseThreadAttachmentEntity entity) {
        return new ExerciseThreadAttachmentResponse(
                entity.getId(),
                entity.getMessage().getId(),
                entity.getType(),
                entity.getFileUrl(),
                entity.getThumbnailUrl(),
                entity.getDurationSeconds(),
                entity.getFileSizeBytes(),
                entity.getMimeType(),
                entity.getUploadedAt()
        );
    }
}
