package dev.jlipka.swiftrestapi.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ImportResult(
        @JsonProperty("entities_in_file") int entitiesInFile,
        @JsonProperty("saved_entities") int savedEntities,
        @JsonProperty("failed_imports") List<FailedImport> failedImports
) {
}
