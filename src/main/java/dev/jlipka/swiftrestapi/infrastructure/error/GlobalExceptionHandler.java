package dev.jlipka.swiftrestapi.infrastructure.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BankNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBankNotFoundException(BankNotFoundException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.name(), ex.getMessage(), Timestamp.from(Instant.now()));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDuplicateResource(DuplicateResourceException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.name(), ex.getMessage(), Timestamp.from(Instant.now()));
    }

    @ExceptionHandler(FileUploadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleFileNotFoundException(FileNotFoundException ex) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), ex.getMessage(), Timestamp.from(Instant.now()));
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ValidationException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.name(), ex.getMessage(), Timestamp.from(Instant.now()));
    }


    @ExceptionHandler(UnsupportedFileTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUnsupportedFileTypeException(UnsupportedFileTypeException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.name(), ex.getMessage(), Timestamp.from(Instant.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

        response.put("status", "BAD_REQUEST");
        response.put("message", "Validation failed");
        response.put("fieldErrors", fieldErrors);
        response.put("timestamp", LocalDateTime.now()
                .toString());

        return ResponseEntity.badRequest()
                .body(response);
    }
}
