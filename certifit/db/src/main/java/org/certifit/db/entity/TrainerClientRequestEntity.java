package org.certifit.db.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.certifit.db.entity.enums.CollaborationType;
import org.certifit.db.entity.enums.TrainerClientRequestStatus;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "trainer_client_requests")
public class TrainerClientRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private UserEntity client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private UserEntity trainer;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "collaboration_type")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private CollaborationType collaborationType;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "trainer_client_request_status", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private TrainerClientRequestStatus status = TrainerClientRequestStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "created_at", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }
}
