package dev.jlipka.swiftrestapi.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@Validated
public class BankController {

    @GetMapping
    public String helloWorld() {
        return "Hello, World";
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importSpreadSheetData(@RequestParam("file")  MultipartFile file){
        String fileName = file.getOriginalFilename();

        if (!fileName.endsWith(".xlsx")) {
            return ResponseEntity.badRequest().body("Uploaded file is not a spreadsheet");
        }


        return ResponseEntity.ok("File uploaded successfully");
    }
}
