package dev.jlipka.swiftrestapi.dto;

import java.util.List;

public record CountryWithBanksResponseDto(String countryISO2, String countryCode, List<BankDetailsResponseDto> swiftCodes) {
}
