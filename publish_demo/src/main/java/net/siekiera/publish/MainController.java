package net.siekiera.publish;

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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class MainController {

    @GetMapping("/status")
    public ResponseEntity<String> healthcheck() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> handleFileUpload(@RequestParam(value = "file") MultipartFile file) throws IOException {
        String uploadFolderName = "/app/static";
        Files.createDirectories(Paths.get(uploadFolderName));
        Path fullPath = Paths.get(uploadFolderName, file.getOriginalFilename());
        file.transferTo(fullPath);
        return new ResponseEntity<>("File uploaded to " + fullPath + "!", HttpStatus.CREATED);
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishImage(@RequestParam(value = "file") MultipartFile file) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        //TODO jak z MultipartFIle zrobic file i wyslac go dalej??
        entityBuilder.addBinaryBody("file", new File("/Users/eric/kody/static_content_provider/sample_images/1.jpg"));
        HttpEntity entity = entityBuilder.build();

        HttpUriRequest request1 = RequestBuilder
                .post("http://localhost:8081/add")
                .setEntity(entity)
                .build();
        HttpUriRequest request2 = RequestBuilder
                .post("http://localhost:8082/add")
                .setEntity(entity)
                .build();
        HttpUriRequest request3 = RequestBuilder
                .post("http://localhost:8083/add")
                .setEntity(entity)
                .build();

        HttpResponse httpresponse1 = httpclient.execute(request1);
        HttpResponse httpresponse2 = httpclient.execute(request2);
        HttpResponse httpresponse3 = httpclient.execute(request3);

        System.out.println("Done successfully!");
        System.out.println(httpresponse1);
        System.out.println(EntityUtils.toString(httpresponse1.getEntity()));

        System.out.println("Done successfully!");
        System.out.println(httpresponse2);
        System.out.println(EntityUtils.toString(httpresponse2.getEntity()));

        System.out.println("Done successfully!");
        System.out.println(httpresponse3);
        System.out.println(EntityUtils.toString(httpresponse3.getEntity()));

        return new ResponseEntity<>("OK", HttpStatus.ACCEPTED);
    }
}
