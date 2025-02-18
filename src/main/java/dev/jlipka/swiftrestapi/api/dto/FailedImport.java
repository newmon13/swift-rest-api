package dev.jlipka.swiftrestapi.api.dto;

import org.springframework.validation.Errors;

import java.util.List;

public record FailedImport(Object entity, List<String> errors) {
}
