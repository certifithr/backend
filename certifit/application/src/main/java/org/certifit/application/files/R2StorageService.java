package org.certifit.application.files;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URI;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class R2StorageService {

    private final R2Properties r2Properties;
    private S3Presigner presigner;

    @PostConstruct
    void init() {
        presigner = S3Presigner.builder()
                .endpointOverride(URI.create("https://" + r2Properties.getAccountId() + ".r2.cloudflarestorage.com"))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(r2Properties.getAccessKey(), r2Properties.getSecretKey())))
                .region(Region.of("auto"))
                .build();
        log.info("R2 storage service initialized for bucket: {}", r2Properties.getBucket());
    }

    public String generatePresignedPutUrl(String key, String mimeType, Duration expiry) {
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(r2Properties.getBucket())
                .key(key)
                .contentType(mimeType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(expiry)
                .putObjectRequest(putRequest)
                .build();

        PresignedPutObjectRequest presigned = presigner.presignPutObject(presignRequest);
        log.debug("Generated presigned PUT URL for key: {}", key);
        return presigned.url().toString();
    }

    public String getPublicUrl(String key) {
        return r2Properties.getPublicUrl() + "/" + key;
    }
}
