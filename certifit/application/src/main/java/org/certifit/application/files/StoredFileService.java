package org.certifit.application.files;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.db.entity.StoredFileEntity;
import org.certifit.db.entity.UserEntity;
import org.certifit.db.repository.StoredFileRepository;
import org.certifit.db.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoredFileService {

    private final StoredFileRepository storedFileRepository;
    private final UserRepository userRepository;
    private final R2StorageService r2StorageService;
    private final R2Properties r2Properties;

    @Transactional
    public PresignedUploadResult presignUpload(UUID userId, String fileName, String mimeType, Long fileSizeBytes) {
        log.info("Generating presigned upload URL for user: {}", userId);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        String key = UUID.randomUUID() + "/" + fileName;
        String presignedUrl = r2StorageService.generatePresignedPutUrl(key, mimeType, Duration.ofMinutes(15));

        StoredFileEntity storedFile = new StoredFileEntity();
        storedFile.setKey(key);
        storedFile.setBucket(r2Properties.getBucket());
        storedFile.setMimeType(mimeType);
        storedFile.setFileSizeBytes(fileSizeBytes);
        storedFile.setUploadedBy(user);

        StoredFileEntity saved = storedFileRepository.save(storedFile);
        log.info("Stored file record created: id={}, key={}", saved.getId(), key);
        return new PresignedUploadResult(saved.getId(), presignedUrl, key);
    }

    @Transactional
    public StoredFileEntity confirmUpload(UUID fileId, UUID userId) {
        log.info("Confirming upload for file: {} by user: {}", fileId, userId);

        StoredFileEntity file = storedFileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("Stored file not found: " + fileId));

        if (!file.getUploadedBy().getId().equals(userId)) {
            throw new IllegalArgumentException("File does not belong to user");
        }

        if (file.getConfirmedAt() != null) {
            throw new IllegalStateException("File already confirmed");
        }

        file.setConfirmedAt(OffsetDateTime.now());
        StoredFileEntity saved = storedFileRepository.save(file);
        log.info("File confirmed: id={}", fileId);
        return saved;
    }

    @Transactional(readOnly = true)
    public String resolvePublicUrl(UUID fileId) {
        StoredFileEntity file = storedFileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("Stored file not found: " + fileId));

        if (file.getConfirmedAt() == null) {
            throw new IllegalStateException("File upload not yet confirmed: " + fileId);
        }

        return r2StorageService.getPublicUrl(file.getKey());
    }

    @Transactional(readOnly = true)
    public StoredFileEntity getById(UUID fileId) {
        return storedFileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("Stored file not found: " + fileId));
    }
}
