package org.certifit.presentation.exerciseform;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.certifit.application.config.JwtAuthenticationFilter.UserPrincipal;
import org.certifit.application.exerciseform.ExerciseThreadAttachmentService;
import org.certifit.presentation.exerciseform.dto.AddAttachmentRequest;
import org.certifit.presentation.exerciseform.dto.ExerciseThreadAttachmentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Exercise Thread Attachments")
@RestController
@RequestMapping("/api/exercise-threads/messages/{messageId}/attachments")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ExerciseThreadAttachmentController {

    private final ExerciseThreadAttachmentService exerciseThreadAttachmentService;
    private final ExerciseFormMapper exerciseFormMapper;

    @Operation(summary = "List attachments for a message")
    @GetMapping
    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    public ResponseEntity<List<ExerciseThreadAttachmentResponse>> getAttachments(@PathVariable UUID messageId) {
        return ResponseEntity.ok(exerciseThreadAttachmentService.getAttachmentsByMessage(messageId)
                .stream().map(exerciseFormMapper::toAttachmentResponse).toList());
    }

    @Operation(summary = "Add an attachment to a message")
    @PostMapping
    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    public ResponseEntity<ExerciseThreadAttachmentResponse> addAttachment(
            @PathVariable UUID messageId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AddAttachmentRequest request
    ) {
        var attachment = exerciseThreadAttachmentService.addAttachment(
                messageId, request.type(), request.storedFileId(),
                request.thumbnailStoredFileId(), request.durationSeconds());
        return ResponseEntity.status(HttpStatus.CREATED).body(exerciseFormMapper.toAttachmentResponse(attachment));
    }

    @Operation(summary = "Delete an attachment")
    @DeleteMapping("/{attachmentId}")
    @PreAuthorize("hasRole('TRAINER') or hasRole('CLIENT')")
    public ResponseEntity<Void> deleteAttachment(
            @PathVariable UUID messageId,
            @PathVariable UUID attachmentId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        exerciseThreadAttachmentService.deleteAttachment(attachmentId);
        return ResponseEntity.noContent().build();
    }
}
