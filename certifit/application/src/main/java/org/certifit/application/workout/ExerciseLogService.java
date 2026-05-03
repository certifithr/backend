package org.certifit.application.workout;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.application.exception.ClientProfileNotFoundException;
import org.certifit.db.entity.ExerciseLogEntity;
import org.certifit.db.entity.WorkoutExerciseEntity;
import org.certifit.db.entity.WorkoutLogEntity;
import org.certifit.db.entity.enums.UserRole;
import org.certifit.db.repository.ClientProfileRepository;
import org.certifit.db.repository.ExerciseLogRepository;
import org.certifit.db.repository.UserRepository;
import org.certifit.db.repository.WorkoutExerciseRepository;
import org.certifit.db.repository.WorkoutLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseLogService {

    private final ExerciseLogRepository exerciseLogRepository;
    private final WorkoutLogRepository workoutLogRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final ClientProfileRepository clientProfileRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ExerciseLogEntity> getLogsByWorkoutLog(UUID workoutLogId) {
        log.debug("Getting exercise logs for workout log: {}", workoutLogId);
        return exerciseLogRepository.findByWorkoutLogId(workoutLogId);
    }

    @Transactional(readOnly = true)
    public ExerciseLogEntity getLogById(UUID logId) {
        log.debug("Getting exercise log by id: {}", logId);
        return exerciseLogRepository.findById(logId)
                .orElseThrow(() -> new IllegalArgumentException("Exercise log not found: " + logId));
    }

    @Transactional
    public ExerciseLogEntity logExercise(UUID workoutLogId, UUID clientId, UUID workoutExerciseId,
                                          Integer setNumber, Integer repsDone, Float weightUsed,
                                          Integer durationSeconds, boolean completed) {
        log.info("Logging exercise set for workout log: {} by client: {}", workoutLogId, clientId);
        validateUserIsClient(clientId);

        WorkoutLogEntity workoutLog = workoutLogRepository.findById(workoutLogId)
                .orElseThrow(() -> new IllegalArgumentException("Workout log not found: " + workoutLogId));

        if (!workoutLog.getClient().getUser().getId().equals(clientId)) {
            throw new IllegalArgumentException("Workout log does not belong to client");
        }

        WorkoutExerciseEntity workoutExercise = workoutExerciseRepository.findById(workoutExerciseId)
                .orElseThrow(() -> new IllegalArgumentException("Workout exercise not found: " + workoutExerciseId));

        ExerciseLogEntity exerciseLog = new ExerciseLogEntity();
        exerciseLog.setWorkoutLog(workoutLog);
        exerciseLog.setWorkoutExercise(workoutExercise);
        exerciseLog.setSetNumber(setNumber);
        exerciseLog.setRepsDone(repsDone);
        exerciseLog.setWeightUsed(weightUsed);
        exerciseLog.setDurationSeconds(durationSeconds);
        exerciseLog.setCompleted(completed);

        ExerciseLogEntity saved = exerciseLogRepository.save(exerciseLog);
        log.info("Exercise log created: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public ExerciseLogEntity updateExerciseLog(UUID logId, UUID clientId, Integer repsDone,
                                                Float weightUsed, Integer durationSeconds, Boolean completed) {
        log.info("Updating exercise log: {} by client: {}", logId, clientId);
        validateUserIsClient(clientId);

        ExerciseLogEntity entity = exerciseLogRepository.findById(logId)
                .orElseThrow(() -> new IllegalArgumentException("Exercise log not found: " + logId));

        if (!entity.getWorkoutLog().getClient().getUser().getId().equals(clientId)) {
            throw new IllegalArgumentException("Exercise log does not belong to client");
        }

        if (repsDone != null) entity.setRepsDone(repsDone);
        if (weightUsed != null) entity.setWeightUsed(weightUsed);
        if (durationSeconds != null) entity.setDurationSeconds(durationSeconds);
        if (completed != null) entity.setCompleted(completed);

        ExerciseLogEntity saved = exerciseLogRepository.save(entity);
        log.info("Exercise log updated: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public void deleteExerciseLog(UUID logId, UUID clientId) {
        log.info("Deleting exercise log: {} by client: {}", logId, clientId);
        validateUserIsClient(clientId);

        ExerciseLogEntity entity = exerciseLogRepository.findById(logId)
                .orElseThrow(() -> new IllegalArgumentException("Exercise log not found: " + logId));

        if (!entity.getWorkoutLog().getClient().getUser().getId().equals(clientId)) {
            throw new IllegalArgumentException("Exercise log does not belong to client");
        }

        exerciseLogRepository.delete(entity);
        log.info("Exercise log deleted: id={}", logId);
    }

    private void validateUserIsClient(UUID userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        if (user.getRole() != UserRole.CLIENT) {
            throw new IllegalArgumentException("User is not a client: " + userId);
        }
    }
}
