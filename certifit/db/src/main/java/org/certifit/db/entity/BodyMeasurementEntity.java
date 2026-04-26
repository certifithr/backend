package org.certifit.db.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "body_measurements")
public class BodyMeasurementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkin_id", nullable = false)
    private ProgressCheckinEntity checkin;

    @Column(name = "weight_kg")
    private Float weightKg;

    @Column(name = "body_fat_pct")
    private Float bodyFatPct;

    @Column(name = "chest_cm")
    private Float chestCm;

    @Column(name = "waist_cm")
    private Float waistCm;

    @Column(name = "hips_cm")
    private Float hipsCm;

    @Column(name = "thigh_cm")
    private Float thighCm;

    @Column(name = "arm_cm")
    private Float armCm;

    @Column(name = "measured_at", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private OffsetDateTime measuredAt;

    @PrePersist
    protected void onCreate() {
        measuredAt = OffsetDateTime.now();
    }
}
