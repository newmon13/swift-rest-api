package dev.jlipka.swiftrestapi.infrastructure.error;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
