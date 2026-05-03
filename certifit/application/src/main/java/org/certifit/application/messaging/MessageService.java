package org.certifit.application.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.application.exception.UserNotFoundException;
import org.certifit.db.entity.MessageEntity;
import org.certifit.db.entity.UserEntity;
import org.certifit.db.repository.MessageRepository;
import org.certifit.db.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<MessageEntity> getMessagesBetweenUsers(UUID userId1, UUID userId2) {
        log.debug("Getting messages between {} and {}", userId1, userId2);
        return messageRepository.findConversation(userId1, userId2);
    }

    @Transactional(readOnly = true)
    public List<MessageEntity> getSentMessages(UUID userId) {
        log.debug("Getting sent messages for {}", userId);
        return messageRepository.findBySenderId(userId);
    }

    @Transactional(readOnly = true)
    public List<MessageEntity> getReceivedMessages(UUID userId) {
        log.debug("Getting received messages for {}", userId);
        return messageRepository.findByReceiverId(userId);
    }

    @Transactional
    public MessageEntity sendMessage(UUID senderId, UUID receiverId, String body) {
        log.info("Sending message from {} to {}", senderId, receiverId);

        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new UserNotFoundException(senderId));

        UserEntity receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new UserNotFoundException(receiverId));

        MessageEntity message = new MessageEntity();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setBody(body);
        message.setRead(false);

        MessageEntity saved = messageRepository.save(message);
        log.info("Message sent: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public void markAsRead(UUID messageId, UUID userId) {
        log.info("Marking message {} as read by {}", messageId, userId);

        MessageEntity message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found: " + messageId));

        if (!message.getReceiver().getId().equals(userId)) {
            throw new IllegalArgumentException("User is not the receiver of this message");
        }

        message.setRead(true);
        messageRepository.save(message);
        log.info("Message marked as read: id={}", messageId);
    }
}
