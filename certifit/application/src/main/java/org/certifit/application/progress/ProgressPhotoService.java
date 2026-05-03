package org.certifit.application.progress;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.application.files.StoredFileService;
import org.certifit.db.entity.ProgressCheckinEntity;
import org.certifit.db.entity.ProgressPhotoEntity;
import org.certifit.db.entity.UserEntity;
import org.certifit.db.entity.enums.PhotoAngle;
import org.certifit.db.entity.enums.UserRole;
import org.certifit.db.repository.ProgressCheckinRepository;
import org.certifit.db.repository.ProgressPhotoRepository;
import org.certifit.db.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProgressPhotoService {

    private final ProgressPhotoRepository progressPhotoRepository;
    private final ProgressCheckinRepository progressCheckinRepository;
    private final UserRepository userRepository;
    private final StoredFileService storedFileService;

    @Transactional(readOnly = true)
    public List<ProgressPhotoEntity> getPhotosByCheckin(UUID checkinId) {
        log.debug("Getting progress photos for checkin: {}", checkinId);
        return progressPhotoRepository.findByCheckinId(checkinId);
    }

    @Transactional
    public ProgressPhotoEntity addPhoto(UUID checkinId, UUID clientId, PhotoAngle angle, UUID storedFileId) {
        log.info("Adding progress photo to checkin {} for client {}", checkinId, clientId);
        validateUserIsClient(clientId);

        ProgressCheckinEntity checkin = progressCheckinRepository.findById(checkinId)
                .orElseThrow(() -> new IllegalArgumentException("Progress checkin not found: " + checkinId));

        if (!checkin.getClient().getUser().getId().equals(clientId)) {
            throw new IllegalArgumentException("Checkin does not belong to client");
        }

        String photoUrl = storedFileService.resolvePublicUrl(storedFileId);

        ProgressPhotoEntity photo = new ProgressPhotoEntity();
        photo.setCheckin(checkin);
        photo.setAngle(angle);
        photo.setPhotoUrl(photoUrl);
        photo.setTakenAt(OffsetDateTime.now());

        ProgressPhotoEntity saved = progressPhotoRepository.save(photo);
        log.info("Progress photo added: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public void deletePhoto(UUID photoId, UUID clientId) {
        log.info("Deleting progress photo {} for client {}", photoId, clientId);
        validateUserIsClient(clientId);

        ProgressPhotoEntity photo = progressPhotoRepository.findById(photoId)
                .orElseThrow(() -> new IllegalArgumentException("Progress photo not found: " + photoId));

        if (!photo.getCheckin().getClient().getUser().getId().equals(clientId)) {
            throw new IllegalArgumentException("Photo does not belong to client");
        }

        progressPhotoRepository.delete(photo);
        log.info("Progress photo deleted: id={}", photoId);
    }

    private void validateUserIsClient(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        if (user.getRole() != UserRole.CLIENT) {
            throw new IllegalArgumentException("User is not a client: " + userId);
        }
    }
}
