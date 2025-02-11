package dev.jlipka.swiftrestapi.validator;

import java.io.File;

public interface FileValidator {
    ValidationResult validate(File file);
}
