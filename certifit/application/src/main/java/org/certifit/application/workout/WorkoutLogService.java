package org.certifit.application.workout;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.application.exception.ClientProfileNotFoundException;
import org.certifit.db.entity.ClientProfileEntity;
import org.certifit.db.entity.UserEntity;
import org.certifit.db.entity.WorkoutAssignmentEntity;
import org.certifit.db.entity.WorkoutDayEntity;
import org.certifit.db.entity.WorkoutLogEntity;
import org.certifit.db.entity.enums.UserRole;
import org.certifit.db.repository.ClientProfileRepository;
import org.certifit.db.repository.UserRepository;
import org.certifit.db.repository.WorkoutAssignmentRepository;
import org.certifit.db.repository.WorkoutDayRepository;
import org.certifit.db.repository.WorkoutLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkoutLogService {

    private final WorkoutLogRepository workoutLogRepository;
    private final ClientProfileRepository clientProfileRepository;
    private final WorkoutAssignmentRepository workoutAssignmentRepository;
    private final WorkoutDayRepository workoutDayRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<WorkoutLogEntity> getWorkoutLogsByClient(UUID clientId) {
        log.debug("Getting workout logs for client: {}", clientId);
        validateUserIsClient(clientId);
        return workoutLogRepository.findByClientId(clientId);
    }

    @Transactional(readOnly = true)
    public WorkoutLogEntity getWorkoutLogById(UUID logId) {
        log.debug("Getting workout log by id: {}", logId);
        return workoutLogRepository.findById(logId)
                .orElseThrow(() -> new IllegalArgumentException("Workout log not found: " + logId));
    }

    @Transactional
    public WorkoutLogEntity createWorkoutLog(UUID clientId, UUID assignmentId, UUID workoutDayId, LocalDate loggedDate, String notes) {
        log.info("Creating workout log for client: {}", clientId);
        validateUserIsClient(clientId);

        ClientProfileEntity clientProfile = clientProfileRepository.findByUserId(clientId)
                .orElseThrow(() -> new ClientProfileNotFoundException(clientId));

        WorkoutAssignmentEntity assignment = null;
        if (assignmentId != null) {
            assignment = workoutAssignmentRepository.findById(assignmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Workout assignment not found: " + assignmentId));

            if (!assignment.getClient().getId().equals(clientProfile.getId())) {
                throw new IllegalArgumentException("Assignment does not belong to client");
            }
        }

        WorkoutDayEntity workoutDay = workoutDayRepository.findById(workoutDayId)
                .orElseThrow(() -> new IllegalArgumentException("Workout day not found: " + workoutDayId));

        WorkoutLogEntity workoutLog = new WorkoutLogEntity();
        workoutLog.setClient(clientProfile);
        workoutLog.setWorkoutAssignment(assignment);
        workoutLog.setWorkoutDay(workoutDay);
        workoutLog.setLoggedDate(loggedDate);
        workoutLog.setNotes(notes);

        WorkoutLogEntity saved = workoutLogRepository.save(workoutLog);
        log.info("Workout log created: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public WorkoutLogEntity updateWorkoutLog(UUID logId, UUID clientId, String notes) {
        log.info("Updating workout log {} for client {}", logId, clientId);
        validateUserIsClient(clientId);

        WorkoutLogEntity workoutLog = workoutLogRepository.findById(logId)
                .orElseThrow(() -> new IllegalArgumentException("Workout log not found: " + logId));

        if (!workoutLog.getClient().getUser().getId().equals(clientId)) {
            throw new IllegalArgumentException("Log does not belong to client");
        }

        if (notes != null) workoutLog.setNotes(notes);

        WorkoutLogEntity saved = workoutLogRepository.save(workoutLog);
        log.info("Workout log updated: id={}", saved.getId());
        return saved;
    }

    private void validateUserIsClient(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        if (user.getRole() != UserRole.CLIENT) {
            throw new IllegalArgumentException("User is not a client: " + userId);
        }
    }
}
