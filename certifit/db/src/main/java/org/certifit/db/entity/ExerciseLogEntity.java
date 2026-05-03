package org.certifit.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "exercise_logs")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_log_id", nullable = false)
    private WorkoutLogEntity workoutLog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_exercise_id", nullable = false)
    private WorkoutExerciseEntity workoutExercise;

    @Column(name = "set_number", nullable = false)
    private Integer setNumber;

    @Column(name = "reps_done")
    private Integer repsDone;

    @Column(name = "weight_used")
    private Float weightUsed;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(nullable = false)
    private boolean completed = true;
}
