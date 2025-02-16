package dev.jlipka.swiftrestapi.dto;

import java.util.List;

public record UploadResponseDto<T>(String message, List<T> entities) {
}
