package dev.jlipka.swiftrestapi.api.mapper.dto;

import dev.jlipka.swiftrestapi.api.dto.Branch;
import dev.jlipka.swiftrestapi.domain.model.Bank;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static java.util.Objects.nonNull;


@Component
public class BranchMapper implements Function<Bank, Branch> {
    @Override
    public Branch apply(Bank bank) {
        return Branch.builder()
                .address(bank.getAddress())
                .bankName(bank.getName())
                .isHeadquarter(nonNull(bank.getHeadquarter()))
                .swiftCode(bank.getSwiftCode())
                .countryISO2(bank.getCountryCode())
                .countryName(bank.getCountryName())
                .build();
    }
}

