package org.certifit.db.repository;

import org.certifit.db.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {

    List<MessageEntity> findBySenderIdOrReceiverIdOrderBySentAtDesc(UUID senderId, UUID receiverId);
}
