package org.certifit.application.progress;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.db.entity.BodyMeasurementEntity;
import org.certifit.db.entity.ProgressCheckinEntity;
import org.certifit.db.entity.UserEntity;
import org.certifit.db.entity.enums.UserRole;
import org.certifit.db.repository.BodyMeasurementRepository;
import org.certifit.db.repository.ProgressCheckinRepository;
import org.certifit.db.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BodyMeasurementService {

    private final BodyMeasurementRepository bodyMeasurementRepository;
    private final ProgressCheckinRepository progressCheckinRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<BodyMeasurementEntity> getMeasurementsByCheckin(UUID checkinId) {
        log.debug("Getting body measurements for checkin: {}", checkinId);
        return bodyMeasurementRepository.findByCheckinId(checkinId);
    }

    @Transactional
    public BodyMeasurementEntity addMeasurement(UUID checkinId, UUID clientId, Float weightKg, Float bodyFatPct, Float chestCm, Float waistCm, Float hipsCm, Float thighCm, Float armCm) {
        log.info("Adding body measurement to checkin {} for client {}", checkinId, clientId);
        validateUserIsClient(clientId);

        ProgressCheckinEntity checkin = progressCheckinRepository.findById(checkinId)
                .orElseThrow(() -> new IllegalArgumentException("Progress checkin not found: " + checkinId));

        if (!checkin.getClient().getUser().getId().equals(clientId)) {
            throw new IllegalArgumentException("Checkin does not belong to client");
        }

        BodyMeasurementEntity measurement = new BodyMeasurementEntity();
        measurement.setCheckin(checkin);
        measurement.setWeightKg(weightKg);
        measurement.setBodyFatPct(bodyFatPct);
        measurement.setChestCm(chestCm);
        measurement.setWaistCm(waistCm);
        measurement.setHipsCm(hipsCm);
        measurement.setThighCm(thighCm);
        measurement.setArmCm(armCm);
        measurement.setMeasuredAt(OffsetDateTime.now());

        BodyMeasurementEntity saved = bodyMeasurementRepository.save(measurement);
        log.info("Body measurement added: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public BodyMeasurementEntity updateMeasurement(UUID measurementId, UUID clientId, Float weightKg, Float bodyFatPct, Float chestCm, Float waistCm, Float hipsCm, Float thighCm, Float armCm) {
        log.info("Updating body measurement {} for client {}", measurementId, clientId);
        validateUserIsClient(clientId);

        BodyMeasurementEntity measurement = bodyMeasurementRepository.findById(measurementId)
                .orElseThrow(() -> new IllegalArgumentException("Body measurement not found: " + measurementId));

        if (!measurement.getCheckin().getClient().getUser().getId().equals(clientId)) {
            throw new IllegalArgumentException("Measurement does not belong to client");
        }

        if (weightKg != null) measurement.setWeightKg(weightKg);
        if (bodyFatPct != null) measurement.setBodyFatPct(bodyFatPct);
        if (chestCm != null) measurement.setChestCm(chestCm);
        if (waistCm != null) measurement.setWaistCm(waistCm);
        if (hipsCm != null) measurement.setHipsCm(hipsCm);
        if (thighCm != null) measurement.setThighCm(thighCm);
        if (armCm != null) measurement.setArmCm(armCm);

        BodyMeasurementEntity saved = bodyMeasurementRepository.save(measurement);
        log.info("Body measurement updated: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public void deleteMeasurement(UUID measurementId, UUID clientId) {
        log.info("Deleting body measurement {} for client {}", measurementId, clientId);
        validateUserIsClient(clientId);

        BodyMeasurementEntity measurement = bodyMeasurementRepository.findById(measurementId)
                .orElseThrow(() -> new IllegalArgumentException("Body measurement not found: " + measurementId));

        if (!measurement.getCheckin().getClient().getUser().getId().equals(clientId)) {
            throw new IllegalArgumentException("Measurement does not belong to client");
        }

        bodyMeasurementRepository.delete(measurement);
        log.info("Body measurement deleted: id={}", measurementId);
    }

    private void validateUserIsClient(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        if (user.getRole() != UserRole.CLIENT) {
            throw new IllegalArgumentException("User is not a client: " + userId);
        }
    }
}
