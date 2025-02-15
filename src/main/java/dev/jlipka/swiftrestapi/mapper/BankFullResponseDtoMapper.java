package dev.jlipka.swiftrestapi.mapper;

import dev.jlipka.swiftrestapi.dto.BankFullDetailsDto;
import dev.jlipka.swiftrestapi.model.Bank;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static java.util.Objects.nonNull;

@Component
public class BankFullResponseDtoMapper implements Function <Bank, BankFullDetailsDto>{
    @Override
    public BankFullDetailsDto apply(Bank bank) {
        return new BankFullDetailsDto(
                bank.getAddress(),
                bank.getName(),
                bank.getCountryCode(),
                bank.getCountryName(),
                nonNull(bank.getHeadquarter()),
                bank.getSwiftCode());
    }
}

