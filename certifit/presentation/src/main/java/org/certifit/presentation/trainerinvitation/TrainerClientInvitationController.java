package org.certifit.presentation.trainerinvitation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.certifit.application.config.JwtAuthenticationFilter.UserPrincipal;
import org.certifit.application.trainerinvitation.TrainerClientInvitationService;
import org.certifit.presentation.trainerinvitation.dto.InvitationResponse;
import org.certifit.presentation.trainerinvitation.dto.InviteByEmailRequest;
import org.certifit.presentation.trainerinvitation.dto.RequestTrainerRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Trainer-Client Invitations")
@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TrainerClientInvitationController {

    private final TrainerClientInvitationService invitationService;
    private final InvitationMapper invitationMapper;

    @Operation(summary = "Client requests to join a trainer")
    @PostMapping("/request-trainer")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<UUID> requestTrainer(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody RequestTrainerRequest request
    ) {
        UUID id = invitationService.requestTrainer(principal.getId(), request.trainerProfileId());
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @Operation(summary = "Trainer invites a person by email")
    @PostMapping("/invite-by-email")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<UUID> inviteByEmail(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody InviteByEmailRequest request
    ) {
        UUID id = invitationService.inviteByEmail(principal.getId(), request.email());
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @Operation(summary = "Client accepts an invitation")
    @PostMapping("/{id}/accept")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> acceptInvitation(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        invitationService.acceptInvitation(id, principal.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Client declines an invitation")
    @PostMapping("/{id}/decline")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> declineInvitation(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        invitationService.declineInvitation(id, principal.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Trainer approves a client's join request")
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<Void> approveRequest(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        invitationService.approveRequest(id, principal.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List pending invitations for the authenticated trainer")
    @GetMapping("/pending-for-trainer")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<List<InvitationResponse>> getPendingForTrainer(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        List<InvitationResponse> responses = invitationService.getPendingForTrainer(principal.getId())
                .stream().map(invitationMapper::toResponse).toList();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "List pending invitations for the authenticated client")
    @GetMapping("/pending-for-client")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<InvitationResponse>> getPendingForClient(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        List<InvitationResponse> responses = invitationService.getPendingForClient(principal.getId())
                .stream().map(invitationMapper::toResponse).toList();
        return ResponseEntity.ok(responses);
    }
}
