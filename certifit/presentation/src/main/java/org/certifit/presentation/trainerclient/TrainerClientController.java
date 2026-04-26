package org.certifit.presentation.trainerclient;

import org.certifit.application.trainerclient.TrainerClientService;
import org.certifit.application.trainerclient.dto.ClientProfileDto;
import org.certifit.application.trainerclient.dto.CreateClientProfileDto;
import org.certifit.application.trainerclient.dto.UpdateClientProfileDto;
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

    // ─── CLIENT MANAGEMENT ────────────────────────────────────────────────────

    /**
     * Trainer adds a new client.
     * POST /api/trainers/{trainerId}/clients
     */
    @PostMapping("/trainers/{trainerId}/clients")
    public ResponseEntity<ClientProfileDto> addClient(
            @PathVariable UUID trainerId,
            @RequestBody CreateClientProfileDto dto
    ) {
        return ResponseEntity.ok(service.addClient(trainerId, dto));
    }

    /**
     * Trainer gets all their clients.
     * GET /api/trainers/{trainerId}/clients
     */
    @GetMapping("/trainers/{trainerId}/clients")
    public ResponseEntity<List<ClientProfileDto>> getClientsByTrainer(
            @PathVariable UUID trainerId
    ) {
        return ResponseEntity.ok(service.getClientsByTrainer(trainerId));
    }

    /**
     * Get client profile.
     * GET /api/clients/{clientId}/profile
     */
    @GetMapping("/clients/{clientId}/profile")
    public ResponseEntity<ClientProfileDto> getClientProfile(
            @PathVariable UUID clientId
    ) {
        return ResponseEntity.ok(service.getClientProfile(clientId));
    }

    /**
     * Update client profile.
     * PUT /api/clients/{clientId}/profile
     */
    @PutMapping("/clients/{clientId}/profile")
    public ResponseEntity<ClientProfileDto> updateClientProfile(
            @PathVariable UUID clientId,
            @RequestBody UpdateClientProfileDto dto
    ) {
        return ResponseEntity.ok(service.updateClientProfile(clientId, dto));
    }
}
