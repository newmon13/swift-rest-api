package dev.jlipka.swiftrestapi.api.dto;

import java.util.List;

public record HeadquarterBankDetailsDto(
        String address,
        String bankName,
        String countryISO2,
        String countryName,
        boolean isHeadquarter,
        String swiftCode,
        List<BranchBankDetailsDto> branches
) implements BankResponseDto {
}
