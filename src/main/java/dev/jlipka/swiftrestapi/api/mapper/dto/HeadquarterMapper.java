package dev.jlipka.swiftrestapi.api.mapper.dto;


import dev.jlipka.swiftrestapi.api.dto.Headquarter;
import dev.jlipka.swiftrestapi.api.dto.SwiftCode;
import dev.jlipka.swiftrestapi.domain.model.Bank;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

import static java.util.Objects.nonNull;

@Component
public class HeadquarterMapper implements BiFunction<Bank, List<Bank>, Headquarter> {

    private final SwiftCodeMapper mapper;

    public HeadquarterMapper(SwiftCodeMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Headquarter apply(Bank bank, List<Bank> branches) {
        List<SwiftCode> branchDtoList = branches.stream()
                .map(mapper)
                .toList();

        return Headquarter.builder()
                .address(bank.getAddress())
                .bankName(bank.getName())
                .isHeadquarter(nonNull(bank.getHeadquarter()))
                .swiftCode(bank.getSwiftCode())
                .countryISO2(bank.getCountryCode())
                .countryName(bank.getCountryName())
                .branches(branchDtoList)
                .build();
    }
}