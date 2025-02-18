package dev.jlipka.swiftrestapi.error;

import org.springframework.validation.ObjectError;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationException extends RuntimeException {

    public ValidationException(List<ObjectError> errors) {
        super(errors.stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", ")));
    }

    public ValidationException(String message) {
        super(message);
    }
}