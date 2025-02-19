package dev.jlipka.swiftrestapi.api.mapper;

import dev.jlipka.swiftrestapi.api.dto.BankDetailsResponseDto;
import dev.jlipka.swiftrestapi.domain.model.Bank;
import org.springframework.stereotype.Component;
import java.util.function.Function;
import static java.util.Objects.isNull;

@Component
public class BankDetailsResponseDtoMapper implements Function<Bank, BankDetailsResponseDto>{
    @Override
    public BankDetailsResponseDto apply(Bank bank) {
        return new BankDetailsResponseDto(
                bank.getAddress(),
                bank.getName(),
                bank.getCountryCode(),
                isNull(bank.getHeadquarter()),
                bank.getSwiftCode());
    }
}
