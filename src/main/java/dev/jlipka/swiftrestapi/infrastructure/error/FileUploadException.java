package dev.jlipka.swiftrestapi.infrastructure.error;

public class FileUploadException extends RuntimeException {
    public FileUploadException(String message) {
        super(message);
    }
}
