package dev.jlipka.swiftrestapi.controller;

import dev.jlipka.swiftrestapi.service.FileManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Validated
public class UploadController {
    @Value("${app.storage.location}")
    private String storageLocation;
    private FileManager fileManager;

    public UploadController(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @PostMapping(value = "/excel/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importExcelFile(@RequestParam("file") MultipartFile file) {
        return fileManager.upload(file);
    }
}