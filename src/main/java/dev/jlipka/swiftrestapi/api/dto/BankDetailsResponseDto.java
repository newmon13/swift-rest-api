package dev.jlipka.swiftrestapi.api.dto;

public record BankDetailsResponseDto(
        String address,
        String bankName,
        String countryISO2,
        boolean isHeadquarter,
        String swiftCode
) {
}
