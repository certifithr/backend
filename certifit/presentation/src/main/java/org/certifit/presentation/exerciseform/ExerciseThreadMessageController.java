package org.certifit.presentation.exerciseform;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.certifit.application.config.JwtAuthenticationFilter.UserPrincipal;
import org.certifit.application.exerciseform.ExerciseThreadMessageService;
import org.certifit.presentation.exerciseform.dto.ExerciseThreadMessageResponse;
import org.certifit.presentation.exerciseform.dto.SendMessageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Exercise Thread Messages")
@RestController
@RequestMapping("/api/exercise-threads/{threadId}/messages")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ExerciseThreadMessageController {

    private final ExerciseThreadMessageService exerciseThreadMessageService;
    private final ExerciseFormMapper exerciseFormMapper;

    @Operation(summary = "List messages in a thread")
    @GetMapping
    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    public ResponseEntity<List<ExerciseThreadMessageResponse>> getMessages(@PathVariable UUID threadId) {
        return ResponseEntity.ok(exerciseThreadMessageService.getMessagesByThread(threadId)
                .stream().map(exerciseFormMapper::toMessageResponse).toList());
    }

    @Operation(summary = "Send a message in a thread")
    @PostMapping
    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    public ResponseEntity<ExerciseThreadMessageResponse> sendMessage(
            @PathVariable UUID threadId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody SendMessageRequest request
    ) {
        var message = exerciseThreadMessageService.sendMessage(threadId, principal.getId(), request.body());
        return ResponseEntity.status(HttpStatus.CREATED).body(exerciseFormMapper.toMessageResponse(message));
    }

    @Operation(summary = "Mark a message as read")
    @PatchMapping("/{messageId}/read")
    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    public ResponseEntity<Void> markAsRead(
            @PathVariable UUID threadId,
            @PathVariable UUID messageId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        exerciseThreadMessageService.markAsRead(messageId, principal.getId());
        return ResponseEntity.noContent().build();
    }
}
