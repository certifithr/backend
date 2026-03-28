package org.certifit.db.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "trainer_profiles")
public class TrainerProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 255)
    private String specialization;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false;

    @Column(name = "verified_at", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime verifiedAt;
}
