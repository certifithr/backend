package org.certifit.presentation.trainerclient;

import org.certifit.application.trainerclient.TrainerClientService;
import org.certifit.application.trainerclient.dto.SendTrainerRequestDto;
import org.certifit.application.trainerclient.dto.TrainerClientDto;
import org.certifit.application.trainerclient.dto.TrainerClientRequestDto;
import org.certifit.application.trainerclient.dto.UpdateTrainerClientDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TrainerClientController {

    private final TrainerClientService service;

    public TrainerClientController(TrainerClientService service) {
        this.service = service;
    }

    // ─── REQUESTS ────────────────────────────────────────────────────────────

    /**
     * Client sends a request to a trainer.
     * POST /api/clients/{clientId}/requests
     */
    @PostMapping("/clients/{clientId}/requests")
    public ResponseEntity<TrainerClientRequestDto> sendRequest(
            @PathVariable UUID clientId,
            @RequestBody SendTrainerRequestDto dto
    ) {
        return ResponseEntity.ok(service.sendRequest(clientId, dto));
    }

    /**
     * Client sees all their sent requests.
     * GET /api/clients/{clientId}/requests
     */
    @GetMapping("/clients/{clientId}/requests")
    public ResponseEntity<List<TrainerClientRequestDto>> getMyRequests(
            @PathVariable UUID clientId
    ) {
        return ResponseEntity.ok(service.getMyRequests(clientId));
    }

    /**
     * Trainer sees all incoming pending requests.
     * GET /api/trainers/{trainerId}/requests
     */
    @GetMapping("/trainers/{trainerId}/requests")
    public ResponseEntity<List<TrainerClientRequestDto>> getIncomingRequests(
            @PathVariable UUID trainerId
    ) {
        return ResponseEntity.ok(service.getIncomingRequests(trainerId));
    }

    /**
     * Trainer accepts a request → creates the relationship.
     * POST /api/requests/{requestId}/accept
     */
    @PostMapping("/requests/{requestId}/accept")
    public ResponseEntity<TrainerClientDto> acceptRequest(
            @PathVariable UUID requestId
    ) {
        return ResponseEntity.ok(service.acceptRequest(requestId));
    }

    /**
     * Trainer rejects a request.
     * POST /api/requests/{requestId}/reject
     */
    @PostMapping("/requests/{requestId}/reject")
    public ResponseEntity<TrainerClientRequestDto> rejectRequest(
            @PathVariable UUID requestId
    ) {
        return ResponseEntity.ok(service.rejectRequest(requestId));
    }

    // ─── RELATIONSHIPS ────────────────────────────────────────────────────────

    /**
     * Trainer sees all their clients.
     * GET /api/trainers/{trainerId}/clients
     */
    @GetMapping("/trainers/{trainerId}/clients")
    public ResponseEntity<List<TrainerClientDto>> getMyClients(
            @PathVariable UUID trainerId
    ) {
        return ResponseEntity.ok(service.getMyClients(trainerId));
    }

    /**
     * Client sees all their trainers.
     * GET /api/clients/{clientId}/trainers
     */
    @GetMapping("/clients/{clientId}/trainers")
    public ResponseEntity<List<TrainerClientDto>> getMyTrainers(
            @PathVariable UUID clientId
    ) {
        return ResponseEntity.ok(service.getMyTrainers(clientId));
    }

    /**
     * Trainer updates relationship status or note (ACTIVE, PAUSED, ENDED).
     * PATCH /api/relations/{relationId}
     */
    @PatchMapping("/relations/{relationId}")
    public ResponseEntity<TrainerClientDto> updateRelation(
            @PathVariable UUID relationId,
            @RequestBody UpdateTrainerClientDto dto
    ) {
        return ResponseEntity.ok(service.updateRelation(relationId, dto));
    }
}
