package org.certifit.presentation.files;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.certifit.application.config.JwtAuthenticationFilter.UserPrincipal;
import org.certifit.application.files.PresignedUploadResult;
import org.certifit.application.files.R2StorageService;
import org.certifit.application.files.StoredFileService;
import org.certifit.db.entity.StoredFileEntity;
import org.certifit.presentation.files.dto.ConfirmUploadRequest;
import org.certifit.presentation.files.dto.PresignUploadRequest;
import org.certifit.presentation.files.dto.PresignUploadResponse;
import org.certifit.presentation.files.dto.StoredFileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// TODO: userId comes from authentication.principal.id
@Tag(name = "Files")
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class StoredFileController {

    private final StoredFileService storedFileService;
    private final R2StorageService r2StorageService;

    @Operation(summary = "Generate presigned upload URL")
    @PostMapping("/presign-upload")
    public ResponseEntity<PresignUploadResponse> presignUpload(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody PresignUploadRequest request
    ) {
        PresignedUploadResult result = storedFileService.presignUpload(
                principal.getId(),
                request.fileName(),
                request.mimeType(),
                request.fileSizeBytes()
        );
        return ResponseEntity.ok(new PresignUploadResponse(result.fileId(), result.presignedUrl(), result.key()));
    }

    @Operation(summary = "Confirm file upload completed")
    @PostMapping("/confirm")
    public ResponseEntity<StoredFileResponse> confirmUpload(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ConfirmUploadRequest request
    ) {
        StoredFileEntity file = storedFileService.confirmUpload(request.fileId(), principal.getId());
        return ResponseEntity.ok(toResponse(file));
    }

    private StoredFileResponse toResponse(StoredFileEntity file) {
        return new StoredFileResponse(
                file.getId(),
                file.getKey(),
                file.getBucket(),
                file.getMimeType(),
                file.getFileSizeBytes(),
                r2StorageService.getPublicUrl(file.getKey()),
                file.getConfirmedAt(),
                file.getCreatedAt()
        );
    }
}
