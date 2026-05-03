package org.certifit.db.repository;

import org.certifit.db.entity.StoredFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoredFileRepository extends JpaRepository<StoredFileEntity, UUID> {
}
