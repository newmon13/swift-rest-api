package dev.jlipka.swiftrestapi.api.dto;

public record UploadResponseDto<T>(String message, int entitiesInFile, int savedEntities) {
}
