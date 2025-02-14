package dev.jlipka.swiftrestapi.dto;

import dev.jlipka.swiftrestapi.validator.ValidationResult;

import java.util.List;

public record UploadResponseDto<T>(ValidationResult validationResult, List<T> entities) {
}
