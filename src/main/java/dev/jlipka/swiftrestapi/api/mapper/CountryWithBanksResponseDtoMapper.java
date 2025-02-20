package dev.jlipka.swiftrestapi.api.mapper;


import dev.jlipka.swiftrestapi.api.dto.BranchBankDetailsDto;
import dev.jlipka.swiftrestapi.api.dto.CountryWithBanksResponseDto;
import dev.jlipka.swiftrestapi.domain.model.Bank;
import org.apache.commons.lang3.function.TriFunction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CountryWithBanksResponseDtoMapper implements TriFunction<String, String, List<Bank>, CountryWithBanksResponseDto> {

    private final BranchBankDetailsDtoMapper mapper;

    public CountryWithBanksResponseDtoMapper(BranchBankDetailsDtoMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public CountryWithBanksResponseDto apply(String countryCode, String countryName, List<Bank> countryBanks) {
        List<BranchBankDetailsDto> countryBankDetailsDtoList = countryBanks.stream()
                .map(mapper)
                .toList();
        return new CountryWithBanksResponseDto(
                countryCode,
                countryName,
                countryBankDetailsDtoList

        );
    }
}
