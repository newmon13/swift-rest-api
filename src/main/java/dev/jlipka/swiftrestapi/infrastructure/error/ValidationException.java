package dev.jlipka.swiftrestapi.infrastructure.error;

import dev.jlipka.swiftrestapi.api.dto.FailedImport;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;
public class ValidationException extends RuntimeException {
    private FailedImport failedImport;

    public ValidationException(List<ObjectError> errors) {
        super(errors.stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", ")));
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(FailedImport failedImport) {
        this.failedImport = failedImport;
    }

    public FailedImport getDetails() {
        return failedImport;
    }
}