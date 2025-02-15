package dev.jlipka.swiftrestapi.mapper;

import dev.jlipka.swiftrestapi.dto.BankDetailsResponseDto;
import dev.jlipka.swiftrestapi.dto.BankWithBranchesResponseDto;
import dev.jlipka.swiftrestapi.dto.CountryWithBanksResponseDto;
import dev.jlipka.swiftrestapi.model.Bank;
import org.apache.commons.lang3.function.TriFunction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CountryWithBanksResponseDtoMapper implements TriFunction<String, String, List<Bank>, CountryWithBanksResponseDto> {
    private final BankDetailsResponseDtoMapper mapper;

    public CountryWithBanksResponseDtoMapper(BankDetailsResponseDtoMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public CountryWithBanksResponseDto apply(String countryCode, String countryName, List<Bank> countryBanks) {
        List<BankDetailsResponseDto> countryBankDetailsDtoList = countryBanks.stream()
                .map(mapper)
                .toList();
        return new CountryWithBanksResponseDto(
                countryCode,
                countryName,
                countryBankDetailsDtoList

        );
    }
}
