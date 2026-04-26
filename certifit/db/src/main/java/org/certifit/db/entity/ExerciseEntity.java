package org.certifit.db.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Entity
@Table(name = "exercises")
public class ExerciseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column
    private Integer externalId;

    @Column
    private String name;

    @Column
    private String slug;

    @Column
    private String category;

    @Column
    private String difficulty;

    @Column
    private String force;

    @Column
    private String mechanic;

    @Column(columnDefinition = "TEXT")
    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "muscles_primary", columnDefinition = "jsonb")
    private List<String> musclesPrimary;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "muscles_secondary", columnDefinition = "jsonb")
    private List<String> musclesSecondary;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "correct_steps", columnDefinition = "jsonb")
    private List<String> correctSteps;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "media", columnDefinition = "jsonb")
    private Object media;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "body_map_images", columnDefinition = "jsonb")
    private Object bodyMapImages;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "variation_of", columnDefinition = "jsonb")
    private Object variationOf;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "variations", columnDefinition = "jsonb")
    private List<Object> variations;

    @Column(name = "created_at", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }
}