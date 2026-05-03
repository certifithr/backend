package org.certifit.application.exerciseform;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.db.entity.ExerciseThreadEntity;
import org.certifit.db.entity.ExerciseThreadMessageEntity;
import org.certifit.db.entity.UserEntity;
import org.certifit.db.repository.ExerciseThreadMessageRepository;
import org.certifit.db.repository.ExerciseThreadRepository;
import org.certifit.db.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseThreadMessageService {

    private final ExerciseThreadMessageRepository exerciseThreadMessageRepository;
    private final ExerciseThreadRepository exerciseThreadRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ExerciseThreadMessageEntity> getMessagesByThread(UUID threadId) {
        log.debug("Getting messages for thread: {}", threadId);
        return exerciseThreadMessageRepository.findByThreadIdOrderBySentAt(threadId);
    }

    @Transactional
    public ExerciseThreadMessageEntity sendMessage(UUID threadId, UUID senderId, String body) {
        log.info("Sending message to thread {} by user {}", threadId, senderId);

        ExerciseThreadEntity thread = exerciseThreadRepository.findById(threadId)
                .orElseThrow(() -> new IllegalArgumentException("Exercise thread not found: " + threadId));

        // Check if sender is part of the thread
        boolean isTrainer = thread.getTrainer().getUser().getId().equals(senderId);
        boolean isClient = thread.getClient().getUser().getId().equals(senderId);

        if (!isTrainer && !isClient) {
            throw new IllegalArgumentException("User is not part of this thread");
        }

        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + senderId));

        ExerciseThreadMessageEntity message = new ExerciseThreadMessageEntity();
        message.setThread(thread);
        message.setSender(sender);
        message.setBody(body);
        message.setRead(false);

        ExerciseThreadMessageEntity saved = exerciseThreadMessageRepository.save(message);
        log.info("Message sent: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public void markAsRead(UUID messageId, UUID userId) {
        log.info("Marking message {} as read by {}", messageId, userId);

        ExerciseThreadMessageEntity message = exerciseThreadMessageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found: " + messageId));

        // Only the other participant can mark as read
        boolean isTrainer = message.getThread().getTrainer().getUser().getId().equals(userId);
        boolean isClient = message.getThread().getClient().getUser().getId().equals(userId);

        if (!isTrainer && !isClient) {
            throw new IllegalArgumentException("User is not part of this thread");
        }

        if (message.getSender().getId().equals(userId)) {
            throw new IllegalArgumentException("Cannot mark own message as read");
        }

        message.setRead(true);
        exerciseThreadMessageRepository.save(message);
        log.info("Message marked as read: id={}", messageId);
    }
}
