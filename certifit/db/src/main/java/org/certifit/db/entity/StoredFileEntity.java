package org.certifit.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.certifit.db.entity.UserEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "stored_files")
@Getter
@Setter
@NoArgsConstructor
public class StoredFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String key;

    @Column(nullable = false, length = 255)
    private String bucket;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    private UserEntity uploadedBy;

    @Column(name = "confirmed_at", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime confirmedAt;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }
}
