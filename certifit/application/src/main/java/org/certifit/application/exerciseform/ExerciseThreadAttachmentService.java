package org.certifit.application.exerciseform;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.application.files.StoredFileService;
import org.certifit.db.entity.ExerciseThreadAttachmentEntity;
import org.certifit.db.entity.ExerciseThreadMessageEntity;
import org.certifit.db.entity.StoredFileEntity;
import org.certifit.db.entity.enums.AttachmentType;
import org.certifit.db.repository.ExerciseThreadAttachmentRepository;
import org.certifit.db.repository.ExerciseThreadMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseThreadAttachmentService {

    private final ExerciseThreadAttachmentRepository exerciseThreadAttachmentRepository;
    private final ExerciseThreadMessageRepository exerciseThreadMessageRepository;
    private final StoredFileService storedFileService;

    @Transactional(readOnly = true)
    public List<ExerciseThreadAttachmentEntity> getAttachmentsByMessage(UUID messageId) {
        log.debug("Getting attachments for message: {}", messageId);
        return exerciseThreadAttachmentRepository.findByMessageId(messageId);
    }

    @Transactional
    public ExerciseThreadAttachmentEntity addAttachment(UUID messageId, AttachmentType type, UUID storedFileId, UUID thumbnailStoredFileId, Integer durationSeconds) {
        log.info("Adding attachment to message {}", messageId);

        ExerciseThreadMessageEntity message = exerciseThreadMessageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found: " + messageId));

        StoredFileEntity file = storedFileService.getById(storedFileId);
        String fileUrl = storedFileService.resolvePublicUrl(storedFileId);
        String thumbnailUrl = thumbnailStoredFileId != null
                ? storedFileService.resolvePublicUrl(thumbnailStoredFileId)
                : null;

        ExerciseThreadAttachmentEntity attachment = new ExerciseThreadAttachmentEntity();
        attachment.setMessage(message);
        attachment.setType(type);
        attachment.setFileUrl(fileUrl);
        attachment.setThumbnailUrl(thumbnailUrl);
        attachment.setDurationSeconds(durationSeconds);
        attachment.setFileSizeBytes(file.getFileSizeBytes());
        attachment.setMimeType(file.getMimeType());
        attachment.setUploadedAt(OffsetDateTime.now());

        ExerciseThreadAttachmentEntity saved = exerciseThreadAttachmentRepository.save(attachment);
        log.info("Attachment added: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public void deleteAttachment(UUID attachmentId) {
        log.info("Deleting attachment {}", attachmentId);

        ExerciseThreadAttachmentEntity attachment = exerciseThreadAttachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new IllegalArgumentException("Attachment not found: " + attachmentId));

        exerciseThreadAttachmentRepository.delete(attachment);
        log.info("Attachment deleted: id={}", attachmentId);
    }
}
