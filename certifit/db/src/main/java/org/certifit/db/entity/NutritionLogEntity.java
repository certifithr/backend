package org.certifit.db.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "nutrition_logs")
public class NutritionLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientProfileEntity client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nutrition_assignment_id")
    private NutritionAssignmentEntity nutritionAssignment;

    @Column(name = "logged_date", nullable = false)
    private LocalDate loggedDate;

    @Column(name = "calories_consumed")
    private Integer caloriesConsumed;

    @Column(name = "protein_g")
    private Float proteinG;

    @Column(name = "carbs_g")
    private Float carbsG;

    @Column(name = "fat_g")
    private Float fatG;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }
}
