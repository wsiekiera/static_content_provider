package net.siekiera.scp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class MainController {
    @Value("${server.name}")
    private String serverName;

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
        return new ResponseEntity<>("[" + serverName + "] File uploaded to path: " + fullPath, HttpStatus.CREATED);
    }
}
