package org.certifit.presentation.workout;

import org.certifit.db.entity.*;
import org.certifit.presentation.workout.dto.*;
import org.springframework.stereotype.Component;

@Component
public class WorkoutMapper {

    public WorkoutPlanResponse toWorkoutPlanResponse(WorkoutPlanEntity entity) {
        return new WorkoutPlanResponse(
                entity.getId(),
                entity.getTrainer().getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getDifficulty(),
                entity.getDurationWeeks(),
                entity.isTemplate(),
                entity.getCreatedAt()
        );
    }

    public WorkoutDayResponse toWorkoutDayResponse(WorkoutDayEntity entity) {
        return new WorkoutDayResponse(
                entity.getId(),
                entity.getWorkoutPlan().getId(),
                entity.getDayNumber(),
                entity.getLabel()
        );
    }

    public WorkoutExerciseResponse toWorkoutExerciseResponse(WorkoutExerciseEntity entity) {
        return new WorkoutExerciseResponse(
                entity.getId(),
                entity.getWorkoutDay().getId(),
                entity.getExercise().getId(),
                entity.getExercise().getName(),
                entity.getOrderIndex(),
                entity.getSets(),
                entity.getReps(),
                entity.getRestSeconds(),
                entity.getWeightKg(),
                entity.getDurationSeconds(),
                entity.getNotes()
        );
    }

    public WorkoutAssignmentResponse toWorkoutAssignmentResponse(WorkoutAssignmentEntity entity) {
        return new WorkoutAssignmentResponse(
                entity.getId(),
                entity.getWorkoutPlan().getId(),
                entity.getWorkoutPlan().getTitle(),
                entity.getClient().getId(),
                entity.getClient().getUser().getId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getStatus(),
                entity.getAssignedAt()
        );
    }

    public WorkoutLogResponse toWorkoutLogResponse(WorkoutLogEntity entity) {
        return new WorkoutLogResponse(
                entity.getId(),
                entity.getClient().getUser().getId(),
                entity.getWorkoutAssignment() != null ? entity.getWorkoutAssignment().getId() : null,
                entity.getWorkoutDay().getId(),
                entity.getLoggedDate(),
                entity.getNotes(),
                entity.getCreatedAt()
        );
    }

    public ExerciseLogResponse toExerciseLogResponse(ExerciseLogEntity entity) {
        return new ExerciseLogResponse(
                entity.getId(),
                entity.getWorkoutLog().getId(),
                entity.getWorkoutExercise().getId(),
                entity.getSetNumber(),
                entity.getRepsDone(),
                entity.getWeightUsed(),
                entity.getDurationSeconds(),
                entity.isCompleted()
        );
    }
}
