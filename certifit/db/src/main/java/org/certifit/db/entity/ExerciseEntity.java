package org.certifit.db.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "exercises")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseEntity {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Column(name = "category")
    private String category;

    @Column(name = "difficulty")
    private String difficulty;

    @Column(name = "force")
    private String force;

    @Column(name = "mechanic")
    private String mechanic;

    @Column(name = "description", columnDefinition = "text")
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
}