package dev.jlipka.swiftrestapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public record BankFullDetailsDto(
        String address,
        @NotBlank @Max(100)
        String bankName,
        @Pattern(regexp = "[A-X]{2}$")
        String countryISO2,
        @NotBlank @Size(min = 4, max = 100)
        String countryName,
        boolean isHeadquarter,
        @NotBlank @Size(min = 11, max = 11)
        String swiftCode
) {
}
