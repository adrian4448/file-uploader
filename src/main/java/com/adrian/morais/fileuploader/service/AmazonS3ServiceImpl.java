package com.adrian.morais.fileuploader.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.nio.ByteBuffer;
import java.time.Duration;

@Service
public class AmazonS3ServiceImpl implements AmazonS3Service {

    private final String ACCESS_KEY = "YOUR_ACESS_KEY";
    private final String SECRET_KEY = "YOUR_SECRET_KEY";
    private final String BUCKET_NAME = "adrian-static-sites";

    @Override
    public void UploadFile(MultipartFile file) {
        try {
            S3Client s3 = S3Client.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(authenticate(ACCESS_KEY, SECRET_KEY)))
                    .region(Region.US_EAST_2)
                    .build();

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(file.getOriginalFilename())
                    .build();

            s3.putObject(objectRequest, RequestBody.fromByteBuffer(ByteBuffer.wrap(file.getBytes())));
        } catch(Exception e) {
            System.out.println("Ocorreu o erro: " + e.getMessage());
        }
    }

    @Override
    public String getUrlDownload(String fileName) {
        S3Presigner presigner = S3Presigner .builder()
                .credentialsProvider(StaticCredentialsProvider.create(authenticate(ACCESS_KEY, SECRET_KEY)))
                .region(Region.US_EAST_2)
                .build();

        PutObjectRequest objectRequest  = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileName)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60))
                .putObjectRequest(objectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);

        return presignedRequest.url().toString();
    }

    private AwsBasicCredentials authenticate(String accessKey, String secretKey) {
        return AwsBasicCredentials.create(
                accessKey,
                secretKey);
    }

}
