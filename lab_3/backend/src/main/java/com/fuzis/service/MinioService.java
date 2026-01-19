package com.fuzis.service;

import io.minio.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.InputStream;
import java.util.UUID;

@ApplicationScoped
public class MinioService {

    final String endpoint = "http://localhost:9000";

    final String accessKey = "admin";

    String secretKey = "admin123";

    String bucketName = "imports";

    private MinioClient minioClient;

    @PostConstruct
    void init() {
        try {
            minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();

            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize MinIO client", e);
        }
    }

    public String uploadFile(InputStream inputStream, String originalFilename, long size) throws Exception {
        String filename = UUID.randomUUID() + "_" + originalFilename;

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(filename)
                        .stream(inputStream, size, -1)
                        .contentType("application/octet-stream")
                        .build()
        );

        return filename;
    }

    public InputStream downloadFile(String filename) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(filename)
                        .build()
        );
    }

    public void deleteFile(String filename) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(filename)
                        .build()
        );
    }
}
