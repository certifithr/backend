package org.certifit.application.trainerclient;

import org.certifit.application.trainerclient.dto.SendTrainerRequestDto;
import org.certifit.application.trainerclient.dto.TrainerClientDto;
import org.certifit.application.trainerclient.dto.TrainerClientRequestDto;
import org.certifit.application.trainerclient.dto.UpdateTrainerClientDto;
import org.certifit.db.entity.TrainerClientEntity;
import org.certifit.db.entity.TrainerClientRequestEntity;
import org.certifit.db.entity.UserEntity;
import org.certifit.db.entity.enums.TrainerClientRequestStatus;
import org.certifit.db.repository.TrainerClientRepository;
import org.certifit.db.repository.TrainerClientRequestRepository;
import org.certifit.db.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TrainerClientService {

    private static final Logger log = LoggerFactory.getLogger(TrainerClientService.class);

    private final TrainerClientRequestRepository requestRepository;
    private final TrainerClientRepository trainerClientRepository;
    private final UserRepository userRepository;

    public TrainerClientService(TrainerClientRequestRepository requestRepository,
                                TrainerClientRepository trainerClientRepository,
                                UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.trainerClientRepository = trainerClientRepository;
        this.userRepository = userRepository;
    }

    // ─── REQUESTS ────────────────────────────────────────────────────────────

    @Transactional
    public TrainerClientRequestDto sendRequest(UUID clientId, SendTrainerRequestDto dto) {
        if (requestRepository.existsByClientIdAndTrainerId(clientId, dto.trainerId())) {
            throw new IllegalStateException("Request already exists for this trainer.");
        }

        UserEntity client = findUser(clientId);
        UserEntity trainer = findUser(dto.trainerId());

        TrainerClientRequestEntity request = new TrainerClientRequestEntity();
        request.setClient(client);
        request.setTrainer(trainer);
        request.setCollaborationType(dto.collaborationType());
        request.setMessage(dto.message());

        return toRequestDto(requestRepository.save(request));
    }

    public List<TrainerClientRequestDto> getIncomingRequests(UUID trainerId) {
        return requestRepository
                .findByTrainerIdAndStatus(trainerId, TrainerClientRequestStatus.PENDING)
                .stream()
                .map(this::toRequestDto)
                .toList();
    }

    public List<TrainerClientRequestDto> getMyRequests(UUID clientId) {
        return requestRepository.findByClientId(clientId)
                .stream()
                .map(this::toRequestDto)
                .toList();
    }

    @Transactional
    public TrainerClientDto acceptRequest(UUID requestId) {
        TrainerClientRequestEntity request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found."));

        if (request.getStatus() != TrainerClientRequestStatus.PENDING) {
            throw new IllegalStateException("Request is no longer pending.");
        }

        request.setStatus(TrainerClientRequestStatus.ACCEPTED);
        requestRepository.save(request);

        TrainerClientEntity relation = new TrainerClientEntity();
        relation.setTrainer(request.getTrainer());
        relation.setClient(request.getClient());
        relation.setRequest(request);
        relation.setCollaborationType(request.getCollaborationType());

        log.info("Trainer {} accepted client {} request {}",
                request.getTrainer().getId(), request.getClient().getId(), requestId);

        return toDto(trainerClientRepository.save(relation));
    }

    @Transactional
    public TrainerClientRequestDto rejectRequest(UUID requestId) {
        TrainerClientRequestEntity request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found."));

        if (request.getStatus() != TrainerClientRequestStatus.PENDING) {
            throw new IllegalStateException("Request is no longer pending.");
        }

        request.setStatus(TrainerClientRequestStatus.REJECTED);
        return toRequestDto(requestRepository.save(request));
    }

    // ─── RELATIONSHIPS ────────────────────────────────────────────────────────

    public List<TrainerClientDto> getMyClients(UUID trainerId) {
        return trainerClientRepository.findByTrainerId(trainerId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<TrainerClientDto> getMyTrainers(UUID clientId) {
        return trainerClientRepository.findByClientId(clientId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public TrainerClientDto updateRelation(UUID relationId, UpdateTrainerClientDto dto) {
        TrainerClientEntity relation = trainerClientRepository.findById(relationId)
                .orElseThrow(() -> new IllegalArgumentException("Relation not found."));

        if (dto.status() != null) relation.setStatus(dto.status());
        if (dto.note() != null) relation.setNote(dto.note());

        return toDto(trainerClientRepository.save(relation));
    }

    // ─── MAPPERS ─────────────────────────────────────────────────────────────

    private TrainerClientRequestDto toRequestDto(TrainerClientRequestEntity e) {
        return new TrainerClientRequestDto(
                e.getId(),
                e.getClient().getId(),
                e.getClient().getFirstName(),
                e.getClient().getLastName(),
                e.getTrainer().getId(),
                e.getTrainer().getFirstName(),
                e.getTrainer().getLastName(),
                e.getCollaborationType(),
                e.getStatus(),
                e.getMessage(),
                e.getCreatedAt()
        );
    }

    private TrainerClientDto toDto(TrainerClientEntity e) {
        return new TrainerClientDto(
                e.getId(),
                e.getTrainer().getId(),
                e.getTrainer().getFirstName(),
                e.getTrainer().getLastName(),
                e.getClient().getId(),
                e.getClient().getFirstName(),
                e.getClient().getLastName(),
                e.getCollaborationType(),
                e.getStatus(),
                e.getNote(),
                e.getCreatedAt()
        );
    }

    private UserEntity findUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }
}
