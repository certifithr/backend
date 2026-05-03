package org.certifit.presentation.files.dto;

import java.util.UUID;

public record PresignUploadResponse(UUID fileId, String presignedUrl, String key) {}
