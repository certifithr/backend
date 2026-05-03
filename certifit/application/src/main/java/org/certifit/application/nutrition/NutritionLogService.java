package org.certifit.application.nutrition;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.application.exception.ClientProfileNotFoundException;
import org.certifit.db.entity.ClientProfileEntity;
import org.certifit.db.entity.NutritionAssignmentEntity;
import org.certifit.db.entity.NutritionLogEntity;
import org.certifit.db.entity.UserEntity;
import org.certifit.db.entity.enums.UserRole;
import org.certifit.db.repository.ClientProfileRepository;
import org.certifit.db.repository.NutritionAssignmentRepository;
import org.certifit.db.repository.NutritionLogRepository;
import org.certifit.db.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NutritionLogService {

    private final NutritionLogRepository nutritionLogRepository;
    private final ClientProfileRepository clientProfileRepository;
    private final NutritionAssignmentRepository nutritionAssignmentRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<NutritionLogEntity> getNutritionLogsByClient(UUID clientId) {
        log.debug("Getting nutrition logs for client: {}", clientId);
        validateUserIsClient(clientId);
        return nutritionLogRepository.findByClientId(clientId);
    }

    @Transactional(readOnly = true)
    public NutritionLogEntity getNutritionLogById(UUID logId) {
        log.debug("Getting nutrition log by id: {}", logId);
        return nutritionLogRepository.findById(logId)
                .orElseThrow(() -> new IllegalArgumentException("Nutrition log not found: " + logId));
    }

    @Transactional
    public NutritionLogEntity createNutritionLog(UUID clientId, UUID assignmentId, LocalDate loggedDate, Integer caloriesConsumed, Float proteinG, Float carbsG, Float fatG, String notes) {
        log.info("Creating nutrition log for client: {}", clientId);
        validateUserIsClient(clientId);

        ClientProfileEntity clientProfile = clientProfileRepository.findByUserId(clientId)
                .orElseThrow(() -> new ClientProfileNotFoundException(clientId));

        NutritionAssignmentEntity assignment = null;
        if (assignmentId != null) {
            assignment = nutritionAssignmentRepository.findById(assignmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Nutrition assignment not found: " + assignmentId));

            if (!assignment.getClient().getId().equals(clientProfile.getId())) {
                throw new IllegalArgumentException("Assignment does not belong to client");
            }
        }

        NutritionLogEntity nutritionLog = new NutritionLogEntity();
        nutritionLog.setClient(clientProfile);
        nutritionLog.setNutritionAssignment(assignment);
        nutritionLog.setLoggedDate(loggedDate);
        nutritionLog.setCaloriesConsumed(caloriesConsumed);
        nutritionLog.setProteinG(proteinG);
        nutritionLog.setCarbsG(carbsG);
        nutritionLog.setFatG(fatG);
        nutritionLog.setNotes(notes);

        NutritionLogEntity saved = nutritionLogRepository.save(nutritionLog);
        log.info("Nutrition log created: id={}", saved.getId());
        return saved;
    }

    @Transactional
    public NutritionLogEntity updateNutritionLog(UUID logId, UUID clientId, Integer caloriesConsumed, Float proteinG, Float carbsG, Float fatG, String notes) {
        log.info("Updating nutrition log {} for client {}", logId, clientId);
        validateUserIsClient(clientId);

        NutritionLogEntity nutritionLog = nutritionLogRepository.findById(logId)
                .orElseThrow(() -> new IllegalArgumentException("Nutrition log not found: " + logId));

        if (!nutritionLog.getClient().getUser().getId().equals(clientId)) {
            throw new IllegalArgumentException("Log does not belong to client");
        }

        if (caloriesConsumed != null) nutritionLog.setCaloriesConsumed(caloriesConsumed);
        if (proteinG != null) nutritionLog.setProteinG(proteinG);
        if (carbsG != null) nutritionLog.setCarbsG(carbsG);
        if (fatG != null) nutritionLog.setFatG(fatG);
        if (notes != null) nutritionLog.setNotes(notes);

        NutritionLogEntity saved = nutritionLogRepository.save(nutritionLog);
        log.info("Nutrition log updated: id={}", saved.getId());
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
