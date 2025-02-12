package dev.jlipka.swiftrestapi.validator;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileValidator {
    ValidationResult validate(MultipartFile file);
}
