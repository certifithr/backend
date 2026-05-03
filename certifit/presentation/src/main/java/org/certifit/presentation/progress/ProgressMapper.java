package org.certifit.presentation.progress;

import lombok.RequiredArgsConstructor;
import org.certifit.db.entity.BodyMeasurementEntity;
import org.certifit.db.entity.ProgressCheckinEntity;
import org.certifit.db.entity.ProgressPhotoEntity;
import org.certifit.presentation.progress.dto.BodyMeasurementResponse;
import org.certifit.presentation.progress.dto.ProgressCheckinResponse;
import org.certifit.presentation.progress.dto.ProgressPhotoResponse;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProgressMapper {

    public ProgressCheckinResponse toCheckinResponse(ProgressCheckinEntity entity) {
        return new ProgressCheckinResponse(
                entity.getId(),
                entity.getClient().getUser().getId(),
                entity.getCheckinDate(),
                entity.getTrainerNote(),
                entity.getClientNote(),
                entity.getCreatedAt()
        );
    }

    public BodyMeasurementResponse toMeasurementResponse(BodyMeasurementEntity entity) {
        return new BodyMeasurementResponse(
                entity.getId(),
                entity.getCheckin().getId(),
                entity.getWeightKg(),
                entity.getBodyFatPct(),
                entity.getChestCm(),
                entity.getWaistCm(),
                entity.getHipsCm(),
                entity.getThighCm(),
                entity.getArmCm(),
                entity.getMeasuredAt()
        );
    }

    public ProgressPhotoResponse toPhotoResponse(ProgressPhotoEntity entity) {
        return new ProgressPhotoResponse(
                entity.getId(),
                entity.getCheckin().getId(),
                entity.getAngle(),
                entity.getPhotoUrl(),
                entity.getTakenAt()
        );
    }
}
