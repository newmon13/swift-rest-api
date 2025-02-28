package dev.jlipka.swiftrestapi.api.mapper.dto;

import dev.jlipka.swiftrestapi.api.dto.BankOffice;
import dev.jlipka.swiftrestapi.domain.model.Bank;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static java.util.Objects.isNull;

@Component
public class BankOfficeMapper implements Function<Bank, BankOffice> {
    @Override
    public BankOffice apply(Bank bank) {
        return new BankOffice(bank.getAddress(), bank.getName(), isNull(bank.getHeadquarter()), bank.getSwiftCode());
    }
}
