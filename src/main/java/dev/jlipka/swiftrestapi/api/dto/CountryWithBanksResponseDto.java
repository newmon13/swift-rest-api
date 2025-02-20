package dev.jlipka.swiftrestapi.api.dto;

import java.util.List;

public record CountryWithBanksResponseDto(String countryISO2, String countryName, List<BranchBankDetailsDto> swiftCodes) {
}
