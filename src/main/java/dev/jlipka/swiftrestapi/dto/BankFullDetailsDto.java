package dev.jlipka.swiftrestapi.dto;

public record BankFullDetailsDto(
        String address,
        String bankName,
        String countryISO2,
        String countryName,
        boolean isHeadquarter,
        String swiftCode
) {
}
