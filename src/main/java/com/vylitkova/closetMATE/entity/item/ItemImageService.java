package com.vylitkova.closetMATE.entity.item;



//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ItemImageService {

    private static final String REMOVE_BG_API_KEY = "cm9VdGF13fP8YdXRLMHFKLs7";

    public byte[] removeBackground(MultipartFile file) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", REMOVE_BG_API_KEY);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image_file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });
        body.add("size", "auto");
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = restTemplate.exchange(
                "https://api.remove.bg/v1.0/removebg",
                HttpMethod.POST,
                requestEntity,
                byte[].class
        );
        return cropImage(response.getBody());
    }
    public byte[] cropImage(byte[] imageData) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        BufferedImage image = ImageIO.read(bais);
        int width = image.getWidth();
        int height = image.getHeight();
        int minX = width, minY = height, maxX = 0, maxY = 0;

        // Проходимо по всіх пікселях, шукаючи межі непрозорого об'єкта
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y) >> 24 & 0xff; // Альфа-канал
                if (pixel > 0) { // Якщо піксель не прозорий
                    if (x < minX) minX = x;
                    if (x > maxX) maxX = x;
                    if (y < minY) minY = y;
                    if (y > maxY) maxY = y;
                }
            }
        }
        if (minX >= maxX || minY >= maxY) {
            return imageData; // Якщо нічого не знайдено, повертаємо оригінальне зображення
        }
        BufferedImage croppedImage = image.getSubimage(minX, minY, maxX - minX + 1, maxY - minY + 1);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(croppedImage, "png", baos);
        return baos.toByteArray();
    }

}
