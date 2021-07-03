package net.siekiera.scp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class MainController {
    @Autowired
    private HttpServletRequest request;

    @GetMapping("/status")
    public ResponseEntity<String> healthcheck() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        String uploadFolderName = "/app/static";
        Files.createDirectories(Paths.get(uploadFolderName));
        Path fullPath = Paths.get(uploadFolderName, file.getOriginalFilename());
        file.transferTo(fullPath);
        return new ResponseEntity<>("File uploaded to " + fullPath + "!", HttpStatus.CREATED);
    }
}
