package dev.jlipka.swiftrestapi.infrastructure.error;

import java.sql.Timestamp;

public record ErrorResponse(String status, String message, Timestamp timestamp) {
}
