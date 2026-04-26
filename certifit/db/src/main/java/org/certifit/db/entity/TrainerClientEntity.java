package org.certifit.db.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.certifit.db.entity.enums.ClientStatus;
import org.certifit.db.entity.enums.CollaborationType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "trainer_clients")
public class TrainerClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private UserEntity trainer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private UserEntity client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private TrainerClientRequestEntity request;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "collaboration_type")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private CollaborationType collaborationType;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "client_status", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private ClientStatus status = ClientStatus.ACTIVE;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "created_at", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }
}
