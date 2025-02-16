package dev.jlipka.swiftrestapi.error;

import org.springframework.validation.ObjectError;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationException extends RuntimeException {
    private final List<ObjectError> errors;

    public ValidationException(List<ObjectError> errors) {
        super(errors.stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", ")));
        this.errors = errors;
    }

    public ValidationException(String message) {
        super(message);
        this.errors = Collections.emptyList();
    }

    public List<ObjectError> getErrors() {
        return errors;
    }
}