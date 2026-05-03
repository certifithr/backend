package org.certifit.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.certifit.db.entity.enums.InvitationStatus;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "trainer_client_invitations")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerClientInvitationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private TrainerProfileEntity trainer;

    @Column(nullable = false, length = 255)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_user_id")
    private UserEntity clientUser;

    @Column(length = 255)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "invitation_status", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private InvitationStatus status;

    @Column(name = "created_at", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private OffsetDateTime createdAt;

    @Column(name = "expires_at", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime expiresAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }
}
