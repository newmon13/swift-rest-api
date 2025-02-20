package dev.jlipka.swiftrestapi.api.mapper;

import dev.jlipka.swiftrestapi.api.dto.BranchBankFullDetailsDto;
import dev.jlipka.swiftrestapi.domain.model.Bank;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static java.util.Objects.nonNull;

@Component
public class BranchBankFullDetailsDtoMapper implements Function <Bank, BranchBankFullDetailsDto>{
    @Override
    public BranchBankFullDetailsDto apply(Bank bank) {
        return new BranchBankFullDetailsDto(
                bank.getAddress(),
                bank.getName(),
                bank.getCountryCode(),
                bank.getCountryName(),
                nonNull(bank.getHeadquarter()),
                bank.getSwiftCode());
    }
}

