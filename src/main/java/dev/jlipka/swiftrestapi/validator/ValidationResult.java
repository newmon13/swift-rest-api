package dev.jlipka.swiftrestapi.validator;

public record ValidationResult(boolean result, String message) {

    public static ValidationResult success(String message) {
        return new ValidationResult(true, message);
    }

    public static ValidationResult error(String message) {
        return new ValidationResult(false, message);
    }
}
