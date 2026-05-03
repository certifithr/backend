package org.certifit.presentation.exerciseform;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.certifit.application.config.JwtAuthenticationFilter.UserPrincipal;
import org.certifit.application.exerciseform.ExerciseThreadService;
import org.certifit.presentation.exerciseform.dto.CreateExerciseThreadRequest;
import org.certifit.presentation.exerciseform.dto.ExerciseThreadResponse;
import org.certifit.presentation.exerciseform.dto.UpdateThreadStatusRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Exercise Form Threads")
@RestController
@RequestMapping("/api/exercise-threads")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ExerciseThreadController {

    private final ExerciseThreadService exerciseThreadService;
    private final ExerciseFormMapper exerciseFormMapper;

    @Operation(summary = "List threads for the authenticated client")
    @GetMapping("/my")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<ExerciseThreadResponse>> getMyThreads(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(exerciseThreadService.getThreadsByClient(principal.getId())
                .stream().map(exerciseFormMapper::toThreadResponse).toList());
    }

    @Operation(summary = "List threads for the authenticated trainer")
    @GetMapping("/trainer")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<List<ExerciseThreadResponse>> getTrainerThreads(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(exerciseThreadService.getThreadsByTrainer(principal.getId())
                .stream().map(exerciseFormMapper::toThreadResponse).toList());
    }

    @Operation(summary = "Get a thread by ID")
    @GetMapping("/{threadId}")
    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    public ResponseEntity<ExerciseThreadResponse> getThreadById(@PathVariable UUID threadId) {
        return ResponseEntity.ok(exerciseFormMapper.toThreadResponse(exerciseThreadService.getThreadById(threadId)));
    }

    @Operation(summary = "Create a form review thread")
    @PostMapping
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<ExerciseThreadResponse> createThread(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateExerciseThreadRequest request
    ) {
        var thread = exerciseThreadService.createThread(principal.getId(), request.exerciseId(), request.clientId());
        return ResponseEntity.status(HttpStatus.CREATED).body(exerciseFormMapper.toThreadResponse(thread));
    }

    @Operation(summary = "Update thread status")
    @PatchMapping("/{threadId}/status")
    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    public ResponseEntity<ExerciseThreadResponse> updateStatus(
            @PathVariable UUID threadId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateThreadStatusRequest request
    ) {
        var thread = exerciseThreadService.updateThreadStatus(threadId, principal.getId(), request.status());
        return ResponseEntity.ok(exerciseFormMapper.toThreadResponse(thread));
    }
}
