package dev.jlipka.swiftrestapi.api.mapper.dto;


import dev.jlipka.swiftrestapi.api.dto.CountrySwiftCodes;
import dev.jlipka.swiftrestapi.api.dto.SwiftCode;
import dev.jlipka.swiftrestapi.domain.model.Bank;
import org.apache.commons.lang3.function.TriFunction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CountrySwiftCodesMapper implements TriFunction<String, String, List<Bank>, CountrySwiftCodes> {

    private final SwiftCodeMapper mapper;

    public CountrySwiftCodesMapper(SwiftCodeMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public CountrySwiftCodes apply(String countryCode, String countryName, List<Bank> countryBanks) {
        List<SwiftCode> mappedSwiftCodes = countryBanks.stream()
                .map(mapper)
                .toList();
        return new CountrySwiftCodes(countryCode, countryName, mappedSwiftCodes

        );
    }
}
