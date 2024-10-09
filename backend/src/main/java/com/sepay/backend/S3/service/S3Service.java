package com.sepay.backend.S3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.UUID;

@Service
public class S3Service {

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    public String uploadFile(MultipartFile profileImg, String fileName) {
        try {
            fileName = createFileName(fileName);
            PutObjectResponse response =
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .contentType(profileImg.getContentType())
                            .build(),
                    RequestBody.fromInputStream(profileImg.getInputStream(), profileImg.getSize()));

            return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    public String createFileName(String fileName){
        return "profile/" + UUID.randomUUID()
                .toString()
                .concat(fileName.substring(fileName.lastIndexOf(".")));
    }

    public String deleteFile(String fileName) {
        System.out.println("Delete profile file: " + fileName);
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            DeleteObjectResponse deleteObjectResponse = s3Client.deleteObject(deleteObjectRequest);

            return String.format("File %s has been deleted successfully.", fileName);
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            throw new RuntimeException("Failed to delete file from S3", e);
        }
    }
}

