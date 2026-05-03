package org.certifit.application.progress;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.application.exception.ClientProfileNotFoundException;
import org.certifit.db.entity.ClientProfileEntity;
import org.certifit.db.entity.ProgressCheckinEntity;
import org.certifit.db.entity.UserEntity;
import org.certifit.db.entity.enums.UserRole;
import org.certifit.db.repository.ClientProfileRepository;
import org.certifit.db.repository.ProgressCheckinRepository;
import org.certifit.db.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProgressCheckinService {

    private final ProgressCheckinRepository progressCheckinRepository;
    private final ClientProfileRepository clientProfileRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public ProgressCheckinEntity getCheckinById(UUID checkinId) {
        return progressCheckinRepository.findById(checkinId)
                .orElseThrow(() -> new IllegalArgumentException("Progress checkin not found: " + checkinId));
    }

    @Transactional(readOnly = true)
    public List<ProgressCheckinEntity> getCheckinsByClient(UUID clientId) {
        log.debug("Getting progress checkins for client: {}", clientId);
        validateUserIsClient(clientId);
        return progressCheckinRepository.findByClientIdOrderByCheckinDateDesc(clientId);
    }

    @Transactional(readOnly = true)
    public Optional<ProgressCheckinEntity> getCheckinByClientAndDate(UUID clientId, LocalDate date) {
        log.debug("Getting progress checkin for client {} on date {}", clientId, date);
        validateUserIsClient(clientId);
        return progressCheckinRepository.findByClientIdAndCheckinDate(clientId, date);
    }

    @Transactional
    public ProgressCheckinEntity createOrUpdateCheckin(UUID clientId, LocalDate checkinDate, String trainerNote, String clientNote) {
        log.info("Creating/updating progress checkin for client {} on {}", clientId, checkinDate);
        validateUserIsClient(clientId);

        ClientProfileEntity clientProfile = clientProfileRepository.findByUserId(clientId)
                .orElseThrow(() -> new ClientProfileNotFoundException(clientId));

        Optional<ProgressCheckinEntity> existing = progressCheckinRepository.findByClientIdAndCheckinDate(clientId, checkinDate);

        ProgressCheckinEntity checkin;
        if (existing.isPresent()) {
            checkin = existing.get();
            if (trainerNote != null) checkin.setTrainerNote(trainerNote);
            if (clientNote != null) checkin.setClientNote(clientNote);
            log.info("Updating existing checkin: id={}", checkin.getId());
        } else {
            checkin = new ProgressCheckinEntity();
            checkin.setClient(clientProfile);
            checkin.setCheckinDate(checkinDate);
            checkin.setTrainerNote(trainerNote);
            checkin.setClientNote(clientNote);
            log.info("Creating new checkin");
        }

        ProgressCheckinEntity saved = progressCheckinRepository.save(checkin);
        return saved;
    }

    @Transactional
    public void deleteCheckin(UUID checkinId, UUID clientId) {
        log.info("Deleting progress checkin {} for client {}", checkinId, clientId);
        validateUserIsClient(clientId);

        ProgressCheckinEntity checkin = progressCheckinRepository.findById(checkinId)
                .orElseThrow(() -> new IllegalArgumentException("Progress checkin not found: " + checkinId));

        if (!checkin.getClient().getUser().getId().equals(clientId)) {
            throw new IllegalArgumentException("Checkin does not belong to client");
        }

        progressCheckinRepository.delete(checkin);
        log.info("Progress checkin deleted: id={}", checkinId);
    }

    private void validateUserIsClient(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        if (user.getRole() != UserRole.CLIENT) {
            throw new IllegalArgumentException("User is not a client: " + userId);
        }
    }
}
