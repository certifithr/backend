package org.certifit.application.workout;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.db.entity.ExerciseEntity;
import org.certifit.db.entity.WorkoutDayEntity;
import org.certifit.db.entity.WorkoutExerciseEntity;
import org.certifit.db.entity.enums.UserRole;
import org.certifit.db.repository.ExerciseRepository;
import org.certifit.db.repository.UserRepository;
import org.certifit.db.repository.WorkoutDayRepository;
import org.certifit.db.repository.WorkoutExerciseRepository;
import org.certifit.db.repository.WorkoutPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkoutExerciseService {

    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final WorkoutDayRepository workoutDayRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<WorkoutExerciseEntity> getExercisesByDay(UUID dayId) {
        log.debug("Getting exercises for workout day: {}", dayId);
        return workoutExerciseRepository.findByWorkoutDayIdOrderByOrderIndex(dayId);
    }

    @Transactional(readOnly = true)
    public WorkoutExerciseEntity getExerciseById(UUID workoutExerciseId) {
        log.debug("Getting workout exercise by id: {}", workoutExerciseId);
        return workoutExerciseRepository.findById(workoutExerciseId)
                .orElseThrow(() -> new IllegalArgumentException("Workout exercise not found: " + workoutExerciseId));
    }

    @Transactional
    public WorkoutExerciseEntity addExercise(UUID dayId, UUID trainerId, UUID exerciseEntityId,
                                              Integer orderIndex, Integer sets, Integer reps,
                                              Integer restSeconds, Float weightKg, Integer durationSeconds, String notes) {
        log.info("Adding exercise {} to day: {} by trainer: {}", exerciseEntityId, dayId, trainerId);

        WorkoutDayEntity day = workoutDayRepository.findById(dayId)
                .orElseThrow(() -> new IllegalArgumentException("Workout day not found: " + dayId));

        validateTrainerOwnersPlan(day.getWorkoutPlan().getId(), trainerId);

        ExerciseEntity exercise = exerciseRepository.findById(exerciseEntityId)
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found: " + exerciseEntityId));

        WorkoutExerciseEntity workoutExercise = new WorkoutExerciseEntity();
        workoutExercise.setWorkoutDay(day);
        workoutExercise.setExercise(exercise);
        workoutExercise.setOrderIndex(orderIndex);
        workoutExercise.setSets(sets);
        workoutExercise.setReps(reps);
        workoutExercise.setRestSeconds(restSeconds);
        workoutExercise.setWeightKg(weightKg);
        workoutExercise.setDurationSeconds(durationSeconds);
        workoutExercise.setNotes(notes);

        WorkoutExerciseEntity saved = workoutExerciseRepository.save(workoutExercise);
        log.info("Workout exercise added: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public WorkoutExerciseEntity updateExercise(UUID workoutExerciseId, UUID trainerId,
                                                 Integer orderIndex, Integer sets, Integer reps,
                                                 Integer restSeconds, Float weightKg, Integer durationSeconds, String notes) {
        log.info("Updating workout exercise: {} by trainer: {}", workoutExerciseId, trainerId);

        WorkoutExerciseEntity workoutExercise = workoutExerciseRepository.findById(workoutExerciseId)
                .orElseThrow(() -> new IllegalArgumentException("Workout exercise not found: " + workoutExerciseId));

        validateTrainerOwnersPlan(workoutExercise.getWorkoutDay().getWorkoutPlan().getId(), trainerId);

        if (orderIndex != null) workoutExercise.setOrderIndex(orderIndex);
        if (sets != null) workoutExercise.setSets(sets);
        if (reps != null) workoutExercise.setReps(reps);
        if (restSeconds != null) workoutExercise.setRestSeconds(restSeconds);
        if (weightKg != null) workoutExercise.setWeightKg(weightKg);
        if (durationSeconds != null) workoutExercise.setDurationSeconds(durationSeconds);
        if (notes != null) workoutExercise.setNotes(notes);

        WorkoutExerciseEntity saved = workoutExerciseRepository.save(workoutExercise);
        log.info("Workout exercise updated: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public void removeExercise(UUID workoutExerciseId, UUID trainerId) {
        log.info("Removing workout exercise: {} by trainer: {}", workoutExerciseId, trainerId);

        WorkoutExerciseEntity workoutExercise = workoutExerciseRepository.findById(workoutExerciseId)
                .orElseThrow(() -> new IllegalArgumentException("Workout exercise not found: " + workoutExerciseId));

        validateTrainerOwnersPlan(workoutExercise.getWorkoutDay().getWorkoutPlan().getId(), trainerId);
        workoutExerciseRepository.delete(workoutExercise);
        log.info("Workout exercise removed: id={}", workoutExerciseId);
    }

    private void validateTrainerOwnersPlan(UUID planId, UUID trainerId) {
        validateUserIsTrainer(trainerId);
        var plan = workoutPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Workout plan not found: " + planId));
        if (!plan.getTrainer().getId().equals(trainerId)) {
            throw new IllegalArgumentException("Trainer does not own this workout plan");
        }
    }

    private void validateUserIsTrainer(UUID userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        if (user.getRole() != UserRole.TRAINER) {
            throw new IllegalArgumentException("User is not a trainer: " + userId);
        }
    }
}
