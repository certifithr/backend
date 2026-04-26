package org.certifit.db.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.certifit.db.entity.enums.PhotoAngle;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "progress_photos")
public class ProgressPhotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkin_id", nullable = false)
    private ProgressCheckinEntity checkin;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "photo_angle", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private PhotoAngle angle;

    @Column(name = "photo_url", nullable = false, columnDefinition = "TEXT")
    private String photoUrl;

    @Column(name = "taken_at", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private OffsetDateTime takenAt;

    @PrePersist
    protected void onCreate() {
        takenAt = OffsetDateTime.now();
    }
}
