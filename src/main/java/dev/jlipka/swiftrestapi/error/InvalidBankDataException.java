package dev.jlipka.swiftrestapi.error;

public class InvalidBankDataException extends RuntimeException {
    public InvalidBankDataException(String message) {
        super(message);
    }
}
