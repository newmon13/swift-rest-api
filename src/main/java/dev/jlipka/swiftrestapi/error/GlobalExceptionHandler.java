package dev.jlipka.swiftrestapi.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BankNotFoundException.class)
    public ErrorResponse handleBankNotFoundException(BankNotFoundException ex) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.name(),
                ex.getMessage(),
                Timestamp.from(Instant.now())
        );
    }
}
