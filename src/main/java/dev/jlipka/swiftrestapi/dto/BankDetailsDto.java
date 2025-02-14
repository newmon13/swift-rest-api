package dev.jlipka.swiftrestapi.dto;

public record BankDetailsDto(
        String address,
        String bankName,
        String countryISO2,
        boolean isHeadquarter,
        String swiftCode
) {
}
