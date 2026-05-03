package org.certifit.application.trainerinvitation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.certifit.application.exception.*;
import org.certifit.db.entity.ClientProfileEntity;
import org.certifit.db.entity.TrainerClientInvitationEntity;
import org.certifit.db.entity.TrainerProfileEntity;
import org.certifit.db.entity.UserEntity;
import org.certifit.db.entity.enums.ClientStatus;
import org.certifit.db.entity.enums.InvitationStatus;
import org.certifit.db.entity.enums.UserRole;
import org.certifit.db.repository.ClientProfileRepository;
import org.certifit.db.repository.TrainerClientInvitationRepository;
import org.certifit.db.repository.TrainerProfileRepository;
import org.certifit.db.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerClientInvitationService {

    private final TrainerClientInvitationRepository invitationRepository;
    private final TrainerProfileRepository trainerProfileRepository;
    private final ClientProfileRepository clientProfileRepository;
    private final UserRepository userRepository;

    // ─── CLIENT REQUESTS TRAINER ──────────────────────────────────────────────

    @Transactional
    public UUID requestTrainer(UUID clientUserId, UUID trainerProfileId) {
        // TODO: verify clientUserId == authenticated user's ID from JWT principal

        UserEntity clientUser = userRepository.findById(clientUserId)
                .orElseThrow(() -> new UserNotFoundException(clientUserId));

        if (clientUser.getRole() != UserRole.CLIENT) {
            throw new InvalidRoleException(clientUserId.toString(), "CLIENT");
        }

        TrainerProfileEntity trainer = trainerProfileRepository.findById(trainerProfileId)
                .orElseThrow(() -> new IllegalArgumentException("Trainer profile not found: " + trainerProfileId));

        if (clientProfileRepository.findByUserId(clientUserId).isPresent()) {
            throw new ClientAlreadyAssignedException(clientUserId);
        }

        if (invitationRepository.existsByTrainer_IdAndClientUser_IdAndStatus(trainerProfileId, clientUserId, InvitationStatus.PENDING)) {
            throw new PendingInvitationAlreadyExistsException("client already sent a request to this trainer");
        }

        TrainerClientInvitationEntity invitation = new TrainerClientInvitationEntity();
        invitation.setTrainer(trainer);
        invitation.setEmail(clientUser.getEmail());
        invitation.setClientUser(clientUser);
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setExpiresAt(OffsetDateTime.now().plusDays(30));

        UUID id = invitationRepository.save(invitation).getId();
        log.info("Client {} requested trainer profile {}", clientUserId, trainerProfileId);
        return id;
    }

    // ─── TRAINER INVITES BY EMAIL ──────────────────────────────────────────────

    @Transactional
    public UUID inviteByEmail(UUID trainerUserId, String email) {
        // TODO: verify trainerUserId == authenticated user's ID from JWT principal

        TrainerProfileEntity trainer = trainerProfileRepository.findByUserId(trainerUserId)
                .orElseThrow(() -> new TrainerProfileNotFoundException(trainerUserId));

        if (invitationRepository.existsByTrainer_IdAndEmailAndStatus(trainer.getId(), email, InvitationStatus.PENDING)) {
            throw new PendingInvitationAlreadyExistsException("invitation for " + email + " already pending for this trainer");
        }

        TrainerClientInvitationEntity invitation = new TrainerClientInvitationEntity();
        invitation.setTrainer(trainer);
        invitation.setEmail(email);
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setExpiresAt(OffsetDateTime.now().plusDays(7));

        userRepository.findByEmail(email).ifPresentOrElse(
                existingUser -> {
                    invitation.setClientUser(existingUser);
                    log.info("Trainer {} invited existing user {} by email", trainerUserId, existingUser.getId());
                },
                () -> {
                    invitation.setToken(UUID.randomUUID().toString());
                    // TODO: send invitation email with token to address: email
                    log.info("Trainer {} invited new email {} — token generated", trainerUserId, email);
                }
        );

        return invitationRepository.save(invitation).getId();
    }

    // ─── CLIENT ACCEPTS ───────────────────────────────────────────────────────

    @Transactional
    public void acceptInvitation(UUID invitationId, UUID clientUserId) {
        // TODO: verify clientUserId == authenticated user's ID from JWT principal

        TrainerClientInvitationEntity invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new InvitationNotFoundException(invitationId));

        ensurePendingAndNotExpired(invitation);

        UserEntity clientUser = userRepository.findById(clientUserId)
                .orElseThrow(() -> new UserNotFoundException(clientUserId));

        if (invitation.getClientUser() == null) {
            // Email-invite flow: user registered after the invite was sent — verify by email
            if (!clientUser.getEmail().equalsIgnoreCase(invitation.getEmail())) {
                throw new UnauthorizedInvitationAccessException();
            }
            invitation.setClientUser(clientUser);
        } else if (!invitation.getClientUser().getId().equals(clientUserId)) {
            throw new UnauthorizedInvitationAccessException();
        }

        if (clientProfileRepository.findByUserId(clientUserId).isPresent()) {
            throw new ClientAlreadyAssignedException(clientUserId);
        }

        createClientProfile(clientUser, invitation.getTrainer());
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitationRepository.save(invitation);
        log.info("Invitation {} accepted by client {}", invitationId, clientUserId);
    }

    // ─── CLIENT DECLINES ──────────────────────────────────────────────────────

    @Transactional
    public void declineInvitation(UUID invitationId, UUID clientUserId) {
        // TODO: verify clientUserId == authenticated user's ID from JWT principal

        TrainerClientInvitationEntity invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new InvitationNotFoundException(invitationId));

        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new InvitationAlreadyProcessedException(invitationId);
        }

        UserEntity clientUser = userRepository.findById(clientUserId)
                .orElseThrow(() -> new UserNotFoundException(clientUserId));

        if (invitation.getClientUser() == null) {
            if (!clientUser.getEmail().equalsIgnoreCase(invitation.getEmail())) {
                throw new UnauthorizedInvitationAccessException();
            }
        } else if (!invitation.getClientUser().getId().equals(clientUserId)) {
            throw new UnauthorizedInvitationAccessException();
        }

        invitation.setStatus(InvitationStatus.DECLINED);
        invitationRepository.save(invitation);
        log.info("Invitation {} declined by client {}", invitationId, clientUserId);
    }

    // ─── TRAINER APPROVES CLIENT REQUEST ──────────────────────────────────────

    @Transactional
    public void approveRequest(UUID invitationId, UUID trainerUserId) {
        // TODO: verify trainerUserId == authenticated user's ID from JWT principal

        TrainerClientInvitationEntity invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new InvitationNotFoundException(invitationId));

        ensurePendingAndNotExpired(invitation);

        if (!invitation.getTrainer().getUser().getId().equals(trainerUserId)) {
            throw new UnauthorizedInvitationAccessException();
        }

        if (invitation.getClientUser() == null) {
            throw new IllegalStateException("Cannot approve an email invitation — client must accept it themselves");
        }

        UUID clientUserId = invitation.getClientUser().getId();
        if (clientProfileRepository.findByUserId(clientUserId).isPresent()) {
            throw new ClientAlreadyAssignedException(clientUserId);
        }

        createClientProfile(invitation.getClientUser(), invitation.getTrainer());
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitationRepository.save(invitation);
        log.info("Invitation {} approved by trainer {}", invitationId, trainerUserId);
    }

    // ─── EXPIRY ───────────────────────────────────────────────────────────────

    @Transactional
    public void expireOldInvitations() {
        List<TrainerClientInvitationEntity> expired = invitationRepository
                .findByStatusAndExpiresAtBefore(InvitationStatus.PENDING, OffsetDateTime.now());

        expired.forEach(inv -> inv.setStatus(InvitationStatus.EXPIRED));
        invitationRepository.saveAll(expired);
        log.info("Expired {} stale invitations", expired.size());
    }

    // ─── QUERIES ──────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<TrainerClientInvitationEntity> getPendingForTrainer(UUID trainerUserId) {
        TrainerProfileEntity trainer = trainerProfileRepository.findByUserId(trainerUserId)
                .orElseThrow(() -> new TrainerProfileNotFoundException(trainerUserId));
        return invitationRepository.findByTrainer_IdAndStatus(trainer.getId(), InvitationStatus.PENDING);
    }

    @Transactional(readOnly = true)
    public List<TrainerClientInvitationEntity> getPendingForClient(UUID clientUserId) {
        return invitationRepository.findByClientUser_IdAndStatus(clientUserId, InvitationStatus.PENDING);
    }

    // ─── HELPERS ──────────────────────────────────────────────────────────────

    private void ensurePendingAndNotExpired(TrainerClientInvitationEntity invitation) {
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new InvitationAlreadyProcessedException(invitation.getId());
        }
        if (invitation.getExpiresAt() != null && OffsetDateTime.now().isAfter(invitation.getExpiresAt())) {
            invitation.setStatus(InvitationStatus.EXPIRED);
            invitationRepository.save(invitation);
            throw new InvitationAlreadyProcessedException(invitation.getId());
        }
    }

    private void createClientProfile(UserEntity clientUser, TrainerProfileEntity trainer) {
        ClientProfileEntity profile = new ClientProfileEntity();
        profile.setUser(clientUser);
        profile.setTrainer(trainer);
        profile.setStatus(ClientStatus.ACTIVE);
        clientProfileRepository.save(profile);
    }
}
