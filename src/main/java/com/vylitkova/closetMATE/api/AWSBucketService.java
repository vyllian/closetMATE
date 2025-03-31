package com.vylitkova.closetMATE.api;

import com.vylitkova.closetMATE.entity.utility.ImageInfo;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Service
public class AWSBucketService {
    private final String bucketName = "closetmatebucket";
    private String accessKey;
    private String secretKey;
    private S3Client s3;

    public AWSBucketService(@Value("${spring.cloud.aws.credentials.access-key}") String accessKey,
                            @Value("${spring.cloud.aws.credentials.secret-key}") String secretKey)
    {   this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.s3= S3Client.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey,secretKey)))
                .build();
    }

    public String uploadToS3(byte[] imageData, String filename, String type) {
        String objectKey;
        if (Objects.equals(type, "item")){
           objectKey  = "item-images/" + filename;
        }else if(Objects.equals(type, "outfit")){
            objectKey  = "outfit-images/" + filename;
        }else{
            objectKey  = "profile-images/" + filename;
        }

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType("image/png")
                .build();
        s3.putObject(putRequest, RequestBody.fromBytes(imageData));
        return filename;
    }
    public void deleteFromS3(String type, String filename) {
        String objectKey;
        if (Objects.equals(type, "item")) {
            objectKey = "item-images/" + filename;
        } else if (Objects.equals(type, "outfit")) {
            objectKey = "outfit-images/" + filename;
        } else {
            objectKey = "profile-images/" + filename;
        }

        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            s3.deleteObject(deleteObjectRequest);
            System.out.println("Файл успішно видалено: " + objectKey);
        } catch (Exception e) {
            System.err.println("Помилка під час видалення файлу: " + objectKey);
            e.printStackTrace();
        }
    }

    public byte[] downloadFromS3(String type, String filename) {
        String objectKey;
        if (Objects.equals(type, "item")){
            objectKey  = "item-images/" + filename;
        }else if(Objects.equals(type, "outfit")){
            objectKey  = "outfit-images/" + filename;
        }else{
            objectKey  = "profile-images/" + filename;
        }
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            InputStream inputStream = s3.getObject(getObjectRequest);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();

        } catch (ObjectNotFoundException e) {
            System.out.println("Файл не знайдено: " + filename);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ImageInfo generatePresignedUrl(String directory, String filename) {
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setName(filename);
        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey,secretKey)))
                .build()) {

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(directory+"/" + filename)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofDays(7)) // URL буде дійсним 7 days
                    .getObjectRequest(getObjectRequest)
                    .build();
            imageInfo.setPublicUrl(presigner.presignGetObject(presignRequest).url().toString());
            imageInfo.setExpirationDate(LocalDateTime.now().plusDays(6));
            return imageInfo;
        }
    }
}
