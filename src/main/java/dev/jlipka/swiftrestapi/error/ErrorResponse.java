package dev.jlipka.swiftrestapi.error;

import java.sql.Timestamp;

public record ErrorResponse(String status, String message, Timestamp timestamp) {
}
