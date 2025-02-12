package dev.jlipka.swiftrestapi.service;

import dev.jlipka.swiftrestapi.dto.UploadResponseDto;
import dev.jlipka.swiftrestapi.validator.FileValidator;
import dev.jlipka.swiftrestapi.validator.ValidationResult;
import dev.jlipka.swiftrestapi.validator.XlsxValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Component
public class FileManager {
    @Value("${app.storage.location}")
    private String storageLocation;
    private FileValidator fileValidator;

    public FileManager() {
        this.fileValidator = new XlsxValidator();
    }

    public ResponseEntity<UploadResponseDto> upload(MultipartFile file) {
        ValidationResult validate = fileValidator.validate(file);

        if (validate.result()) {
            try {
                Path uploadPath = Path.of(storageLocation);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(Objects.requireNonNull(file.getOriginalFilename()));
                file.transferTo(new File(String.valueOf(filePath)));

                return ResponseEntity.ok().body(new UploadResponseDto(validate));
            } catch (IOException e) {
                return ResponseEntity.internalServerError()
                        .body(new UploadResponseDto(new ValidationResult(false, "I/O exception occurred")));
            }
        } else {
            return ResponseEntity.badRequest()
                    .body(new UploadResponseDto(new ValidationResult(false, "I/O exception occurred")));
        }
    }
}
