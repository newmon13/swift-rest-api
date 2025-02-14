package dev.jlipka.swiftrestapi.dto;

import java.util.List;

public record BankWithBranchesDto(
        String address,
        String bankName,
        String countryISO2,
        String countryName,
        boolean isHeadquarter,
        String swiftCode,
        List<BankDetailsDto> branches
) {
}
