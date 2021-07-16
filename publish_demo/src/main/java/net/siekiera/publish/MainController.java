package net.siekiera.publish;

import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@RestController
public class MainController {
    @Value("${bucket.name}")
    private String bucketName;
    @Value("${s3.storage.path}")
    private String storagePath;


    @GetMapping("/status")
    public ResponseEntity<String> healthcheck() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishImage(@RequestParam("file") MultipartFile file, @RequestParam("contentHash") String contentHash) throws IOException {
        String storageKey = String.join("/", List.of(storagePath, contentHash, Objects.requireNonNull(file.getOriginalFilename())));
        S3Client amazonS3 = S3Client.builder()
                .region(Region.EU_WEST_1)
                .build();
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(storageKey)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .contentType("image/jpeg")
                .build();
        PutObjectResponse response = amazonS3
                .putObject(request, RequestBody.fromBytes(file.getBytes()));
        URL contentUrl = amazonS3.utilities().getUrl(GetUrlRequest.builder().bucket(bucketName).key(storageKey).build());
        return new ResponseEntity<>(contentUrl.toString(), HttpStatus.ACCEPTED);
    }
}
