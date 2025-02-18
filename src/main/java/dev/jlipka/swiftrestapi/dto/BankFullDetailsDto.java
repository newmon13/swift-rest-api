package dev.jlipka.swiftrestapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BankFullDetailsDto(
        @Max(value = 100, message = "Address cannot be longer than 100 characters")
        String address,
        @Max(value = 100, message = "Bank name cannot be longer than 100 characters")
        String bankName,
        @Pattern(regexp = "[A-X]{2}$", message = "Invalid format of ISO2 country code")
        String countryISO2,
        @Size(min = 4, max = 100, message = "Country name should be at least 4 characters long and maximum 100 characters long")
        String countryName,
        boolean isHeadquarter,
        @NotBlank @Size(min = 11, max = 11, message = "Application accept only 11 characters long SWIFT code")
        String swiftCode
) {
}
