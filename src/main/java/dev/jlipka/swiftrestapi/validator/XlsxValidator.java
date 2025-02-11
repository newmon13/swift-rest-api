package dev.jlipka.swiftrestapi.validator;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class XlsxValidator implements FileValidator {

    @Override
    public ValidationResult validate(File file) {
        return isXlsxFile(file.getName());
    }

    private ValidationResult isXlsxFile(String fileName) {
        if (fileName.toLowerCase().endsWith(".xlsx")) {
            return ValidationResult.success("File validated successfully");
        } else {
            return ValidationResult.error("File is not .xlsx");
        }
    }
}