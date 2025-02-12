package dev.jlipka.swiftrestapi.dto;

import dev.jlipka.swiftrestapi.validator.ValidationResult;

import java.util.List;

public record UploadResponseDto(ValidationResult validationResult) {
}
