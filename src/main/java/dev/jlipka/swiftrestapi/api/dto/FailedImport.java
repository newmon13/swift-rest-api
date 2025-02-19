package dev.jlipka.swiftrestapi.api.dto;

import java.util.List;

public record FailedImport(Object entity, List<String> errors) {
}
