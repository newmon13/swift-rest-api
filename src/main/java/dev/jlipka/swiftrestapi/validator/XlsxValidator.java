package dev.jlipka.swiftrestapi.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Component
public class XlsxValidator implements FileValidator {
    private static final int maxSizeInMb = 5;

    @Override
    public ValidationResult validate(MultipartFile file) {
        return isXlsxFile(file);
    }

    private ValidationResult isXlsxFile(MultipartFile file) {
        if (Objects.requireNonNull(file.getOriginalFilename())
                .toLowerCase().endsWith(".xlsx")) {
            return isFileTooBig(file);
        } else {
            return ValidationResult.error("File is not .xlsx");
        }
    }

    private ValidationResult isFileTooBig(MultipartFile file) {
        if (calculateSizeInBytesToMb(file.getSize()) < maxSizeInMb) {
            return ValidationResult.success("Successfully validated file");
        } else {
            return ValidationResult.error(String.format("File is too large (Max: %d MB)", maxSizeInMb));
        }
    }

    private long calculateSizeInBytesToMb(long bytes) {
        return bytes / (1024 * 1024);
    }
}