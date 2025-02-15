package dev.jlipka.swiftrestapi.mapper;

import dev.jlipka.swiftrestapi.dto.BankDetailsResponseDto;
import dev.jlipka.swiftrestapi.model.Bank;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static java.util.Objects.nonNull;

@Component
public class BankDetailsResponseDtoMapper implements Function<Bank, BankDetailsResponseDto>{
    @Override
    public BankDetailsResponseDto apply(Bank bank) {
        return new BankDetailsResponseDto(
                bank.getAddress(),
                bank.getName(),
                bank.getCountryCode(),
                nonNull(bank.getHeadquarter()),
                bank.getSwiftCode());
    }
}
