package org.certifit.application.files;

import java.util.UUID;

public record PresignedUploadResult(UUID fileId, String presignedUrl, String key) {}
