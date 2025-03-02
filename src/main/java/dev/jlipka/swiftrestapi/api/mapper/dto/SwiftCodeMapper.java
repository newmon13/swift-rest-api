package dev.jlipka.swiftrestapi.api.mapper.dto;

import dev.jlipka.swiftrestapi.api.dto.SwiftCode;
import dev.jlipka.swiftrestapi.domain.model.Bank;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static java.util.Objects.isNull;

@Component
public class SwiftCodeMapper implements Function<Bank, SwiftCode> {

    @Override
    public SwiftCode apply(Bank bank) {
        return new SwiftCode(bank.getAddress(), bank.getName(), isNull(bank.getHeadquarter()), bank.getSwiftCode(), bank.getCountryCode());
    }
}
