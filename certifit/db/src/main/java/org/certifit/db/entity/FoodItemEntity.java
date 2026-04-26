package org.certifit.db.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "food_items")
public class FoodItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private MealEntity meal;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private Float quantity;

    @Column(nullable = false, length = 30)
    private String unit;

    private Integer calories;

    @Column(name = "protein_g")
    private Float proteinG;

    @Column(name = "carbs_g")
    private Float carbsG;

    @Column(name = "fat_g")
    private Float fatG;
}
