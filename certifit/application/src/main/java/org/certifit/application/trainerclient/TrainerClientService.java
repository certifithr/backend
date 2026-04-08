package org.certifit.application.trainerclient;

import org.certifit.application.exception.*;
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
        log.debug("Client {} attempting to send request to trainer {}", clientId, dto.trainerId());

        if (requestRepository.existsByClientIdAndTrainerId(clientId, dto.trainerId())) {
            log.warn("Duplicate request attempt - client: {}, trainer: {}", clientId, dto.trainerId());
            throw new RequestAlreadyExistsException(clientId, dto.trainerId());
        }

        UserEntity client = findUser(clientId);
        UserEntity trainer = findUser(dto.trainerId());

        TrainerClientRequestEntity request = new TrainerClientRequestEntity();
        request.setClient(client);
        request.setTrainer(trainer);
        request.setCollaborationType(dto.collaborationType());
        request.setMessage(dto.message());

        TrainerClientRequestEntity savedRequest = requestRepository.save(request);
        log.info("Client {} sent request to trainer {} with ID {}", clientId, dto.trainerId(), savedRequest.getId());

        return toRequestDto(savedRequest);
    }

    public List<TrainerClientRequestDto> getIncomingRequests(UUID trainerId) {
        log.debug("Fetching incoming pending requests for trainer {}", trainerId);
        List<TrainerClientRequestDto> requests = requestRepository
                .findByTrainerIdAndStatus(trainerId, TrainerClientRequestStatus.PENDING)
                .stream()
                .map(this::toRequestDto)
                .toList();
        log.debug("Found {} pending requests for trainer {}", requests.size(), trainerId);
        return requests;
    }

    public List<TrainerClientRequestDto> getMyRequests(UUID clientId) {
        log.debug("Fetching all requests sent by client {}", clientId);
        List<TrainerClientRequestDto> requests = requestRepository.findByClientId(clientId)
                .stream()
                .map(this::toRequestDto)
                .toList();
        log.debug("Found {} requests sent by client {}", requests.size(), clientId);
        return requests;
    }

    @Transactional
    public TrainerClientDto acceptRequest(UUID requestId) {
        log.debug("Attempting to accept request {}", requestId);

        TrainerClientRequestEntity request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException(requestId));

        if (request.getStatus() != TrainerClientRequestStatus.PENDING) {
            log.warn("Request {} is not in PENDING state, current status: {}", requestId, request.getStatus());
            throw new InvalidRequestStateException(requestId, request.getStatus());
        }

        request.setStatus(TrainerClientRequestStatus.ACCEPTED);
        requestRepository.save(request);

        TrainerClientEntity relation = new TrainerClientEntity();
        relation.setTrainer(request.getTrainer());
        relation.setClient(request.getClient());
        relation.setRequest(request);
        relation.setCollaborationType(request.getCollaborationType());

        TrainerClientEntity savedRelation = trainerClientRepository.save(relation);

        log.info("Trainer {} accepted client {} request {}, created relation {}",
                request.getTrainer().getId(), request.getClient().getId(), requestId, savedRelation.getId());

        return toDto(savedRelation);
    }

    @Transactional
    public TrainerClientRequestDto rejectRequest(UUID requestId) {
        log.debug("Attempting to reject request {}", requestId);

        TrainerClientRequestEntity request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException(requestId));

        if (request.getStatus() != TrainerClientRequestStatus.PENDING) {
            log.warn("Request {} is not in PENDING state, current status: {}", requestId, request.getStatus());
            throw new InvalidRequestStateException(requestId, request.getStatus());
        }

        request.setStatus(TrainerClientRequestStatus.REJECTED);
        TrainerClientRequestEntity savedRequest = requestRepository.save(request);

        log.info("Trainer {} rejected client {} request {}",
                request.getTrainer().getId(), request.getClient().getId(), requestId);

        return toRequestDto(savedRequest);
    }

    // ─── RELATIONSHIPS ────────────────────────────────────────────────────────

    public List<TrainerClientDto> getMyClients(UUID trainerId) {
        log.debug("Fetching clients for trainer {}", trainerId);
        List<TrainerClientDto> clients = trainerClientRepository.findByTrainerId(trainerId)
                .stream()
                .map(this::toDto)
                .toList();
        log.debug("Found {} clients for trainer {}", clients.size(), trainerId);
        return clients;
    }

    public List<TrainerClientDto> getMyTrainers(UUID clientId) {
        log.debug("Fetching trainers for client {}", clientId);
        List<TrainerClientDto> trainers = trainerClientRepository.findByClientId(clientId)
                .stream()
                .map(this::toDto)
                .toList();
        log.debug("Found {} trainers for client {}", trainers.size(), clientId);
        return trainers;
    }

    @Transactional
    public TrainerClientDto updateRelation(UUID relationId, UpdateTrainerClientDto dto) {
        log.debug("Attempting to update relation {} with dto: {}", relationId, dto);

        TrainerClientEntity relation = trainerClientRepository.findById(relationId)
                .orElseThrow(() -> new RelationNotFoundException(relationId));

        if (dto.status() != null) {
            log.debug("Updating relation {} status from {} to {}", relationId, relation.getStatus(), dto.status());
            relation.setStatus(dto.status());
        }
        if (dto.note() != null) {
            log.debug("Updating relation {} note", relationId);
            relation.setNote(dto.note());
        }

        TrainerClientEntity savedRelation = trainerClientRepository.save(relation);
        log.info("Updated relation {} - trainer: {}, client: {}",
                relationId, relation.getTrainer().getId(), relation.getClient().getId());

        return toDto(savedRelation);
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
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
