package org.certifit.application.trainerclient;

import org.certifit.application.exception.*;
import org.certifit.application.trainerclient.dto.ClientProfileDto;
import org.certifit.application.trainerclient.dto.CreateClientProfileDto;
import org.certifit.application.trainerclient.dto.UpdateClientProfileDto;
import org.certifit.db.entity.ClientProfileEntity;
import org.certifit.db.entity.TrainerProfileEntity;
import org.certifit.db.entity.UserEntity;
import org.certifit.db.entity.enums.ClientStatus;
import org.certifit.db.entity.enums.GenderType;
import org.certifit.db.entity.enums.UserRole;
import org.certifit.db.repository.ClientProfileRepository;
import org.certifit.db.repository.TrainerProfileRepository;
import org.certifit.db.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TrainerClientService {

    private static final Logger log = LoggerFactory.getLogger(TrainerClientService.class);

    private final ClientProfileRepository clientProfileRepository;
    private final TrainerProfileRepository trainerProfileRepository;
    private final UserRepository userRepository;

    public TrainerClientService(ClientProfileRepository clientProfileRepository,
                                TrainerProfileRepository trainerProfileRepository,
                                UserRepository userRepository) {
        this.clientProfileRepository = clientProfileRepository;
        this.trainerProfileRepository = trainerProfileRepository;
        this.userRepository = userRepository;
    }

    // ─── CLIENT MANAGEMENT ────────────────────────────────────────────────────

    @Transactional
    public ClientProfileDto addClient(UUID trainerId, CreateClientProfileDto dto) {
        log.debug("Trainer {} adding client {}", trainerId, dto.userId());

        UserEntity trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new UserNotFoundException(trainerId));

        if (!trainer.getRole().equals(UserRole.TRAINER)) {
            throw new InvalidRoleException(trainerId.toString(), "TRAINER");
        }

        UserEntity clientUser = userRepository.findById(dto.userId())
                .orElseThrow(() -> new UserNotFoundException(dto.userId()));

        if (!clientUser.getRole().equals(UserRole.CLIENT)) {
            throw new InvalidRoleException(dto.userId().toString(), "CLIENT");
        }

        TrainerProfileEntity trainerProfile = trainerProfileRepository.findByUserId(trainerId)
                .orElseThrow(() -> new TrainerProfileNotFoundException(trainerId));

        if (clientProfileRepository.findByUserId(dto.userId()).isPresent()) {
            throw new ClientAlreadyAssignedException(dto.userId());
        }

        ClientProfileEntity clientProfile = new ClientProfileEntity();
        clientProfile.setUser(clientUser);
        clientProfile.setTrainer(trainerProfile);
        clientProfile.setDateOfBirth(dto.dateOfBirth());
        clientProfile.setGender(GenderType.valueOf(dto.gender().toUpperCase()));
        clientProfile.setHeightCm(dto.heightCm());
        clientProfile.setGoal(dto.goal());
        clientProfile.setStatus(ClientStatus.ACTIVE);

        ClientProfileEntity saved = clientProfileRepository.save(clientProfile);
        log.info("Client {} added to trainer {}", dto.userId(), trainerId);

        return toDto(saved);
    }

    public List<ClientProfileDto> getClientsByTrainer(UUID trainerId) {
        log.debug("Getting clients for trainer {}", trainerId);

        List<ClientProfileEntity> clients = clientProfileRepository.findByTrainerId(trainerId);
        return clients.stream().map(this::toDto).collect(Collectors.toList());
    }

    public ClientProfileDto getClientProfile(UUID clientId) {
        log.debug("Getting client profile for {}", clientId);

        ClientProfileEntity client = clientProfileRepository.findByUserId(clientId)
                .orElseThrow(() -> new ClientProfileNotFoundException(clientId));

        return toDto(client);
    }

    @Transactional
    public ClientProfileDto updateClientProfile(UUID clientId, UpdateClientProfileDto dto) {
        log.debug("Updating client profile for {}", clientId);

        ClientProfileEntity client = clientProfileRepository.findByUserId(clientId)
                .orElseThrow(() -> new ClientProfileNotFoundException(clientId));

        // Update fields
        if (dto.dateOfBirth() != null) client.setDateOfBirth(dto.dateOfBirth());
        if (dto.gender() != null) client.setGender(GenderType.valueOf(dto.gender().toUpperCase()));
        if (dto.heightCm() != null) client.setHeightCm(dto.heightCm());
        if (dto.goal() != null) client.setGoal(dto.goal());
        if (dto.status() != null) client.setStatus(ClientStatus.valueOf(dto.status().toUpperCase()));

        ClientProfileEntity saved = clientProfileRepository.save(client);
        log.info("Client profile updated for {}", clientId);

        return toDto(saved);
    }

    private ClientProfileDto toDto(ClientProfileEntity entity) {
        return new ClientProfileDto(
                entity.getId(),
                entity.getUser().getId(),
                entity.getTrainer().getId(),
                entity.getDateOfBirth(),
                entity.getGender() != null ? entity.getGender().name() : null,
                entity.getHeightCm(),
                entity.getGoal(),
                entity.getStatus() != null ? entity.getStatus().name() : null,
                entity.getOnboardedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
