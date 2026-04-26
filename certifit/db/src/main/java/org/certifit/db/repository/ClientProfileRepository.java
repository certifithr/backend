package org.certifit.db.repository;

import org.certifit.db.entity.ClientProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientProfileRepository extends JpaRepository<ClientProfileEntity, UUID> {

    Optional<ClientProfileEntity> findByUserId(UUID userId);

    List<ClientProfileEntity> findByTrainerId(UUID trainerId);
}
