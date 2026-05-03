package org.certifit.application.workout;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.db.entity.WorkoutDayEntity;
import org.certifit.db.entity.WorkoutPlanEntity;
import org.certifit.db.entity.enums.UserRole;
import org.certifit.db.repository.UserRepository;
import org.certifit.db.repository.WorkoutDayRepository;
import org.certifit.db.repository.WorkoutPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkoutDayService {

    private final WorkoutDayRepository workoutDayRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<WorkoutDayEntity> getDaysByPlan(UUID planId) {
        log.debug("Getting days for workout plan: {}", planId);
        return workoutDayRepository.findByWorkoutPlanIdOrderByDayNumber(planId);
    }

    @Transactional(readOnly = true)
    public WorkoutDayEntity getDayById(UUID dayId) {
        log.debug("Getting workout day by id: {}", dayId);
        return workoutDayRepository.findById(dayId)
                .orElseThrow(() -> new IllegalArgumentException("Workout day not found: " + dayId));
    }

    @Transactional
    public WorkoutDayEntity createDay(UUID planId, UUID trainerId, Integer dayNumber, String label) {
        log.info("Creating workout day for plan: {} by trainer: {}", planId, trainerId);
        validateTrainerOwnersPlan(planId, trainerId);

        WorkoutPlanEntity plan = workoutPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Workout plan not found: " + planId));

        WorkoutDayEntity day = new WorkoutDayEntity();
        day.setWorkoutPlan(plan);
        day.setDayNumber(dayNumber);
        day.setLabel(label);

        WorkoutDayEntity saved = workoutDayRepository.save(day);
        log.info("Workout day created: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public WorkoutDayEntity updateDay(UUID dayId, UUID trainerId, Integer dayNumber, String label) {
        log.info("Updating workout day: {} by trainer: {}", dayId, trainerId);

        WorkoutDayEntity day = workoutDayRepository.findById(dayId)
                .orElseThrow(() -> new IllegalArgumentException("Workout day not found: " + dayId));

        validateTrainerOwnersPlan(day.getWorkoutPlan().getId(), trainerId);

        if (dayNumber != null) day.setDayNumber(dayNumber);
        if (label != null) day.setLabel(label);

        WorkoutDayEntity saved = workoutDayRepository.save(day);
        log.info("Workout day updated: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public void deleteDay(UUID dayId, UUID trainerId) {
        log.info("Deleting workout day: {} by trainer: {}", dayId, trainerId);

        WorkoutDayEntity day = workoutDayRepository.findById(dayId)
                .orElseThrow(() -> new IllegalArgumentException("Workout day not found: " + dayId));

        validateTrainerOwnersPlan(day.getWorkoutPlan().getId(), trainerId);
        workoutDayRepository.delete(day);
        log.info("Workout day deleted: id={}", dayId);
    }

    private void validateTrainerOwnersPlan(UUID planId, UUID trainerId) {
        validateUserIsTrainer(trainerId);
        WorkoutPlanEntity plan = workoutPlanRepository.findById(planId)
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
