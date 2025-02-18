package dev.jlipka.swiftrestapi.infrastructure.error;

public class UnsupportedFileTypeException extends RuntimeException {
    public UnsupportedFileTypeException(String message) {
        super(message);
    }
}
