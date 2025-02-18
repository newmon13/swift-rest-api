package dev.jlipka.swiftrestapi.api.dto;

import java.util.List;

public record ImportResult(int entitiesInFile, int savedEntities, List<FailedImport> failedImports) {
}
