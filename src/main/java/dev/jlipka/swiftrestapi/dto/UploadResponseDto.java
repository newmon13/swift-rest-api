package dev.jlipka.swiftrestapi.dto;

public record UploadResponseDto<T>(String message, int entitiesInFile, int savedEntities) {
}
