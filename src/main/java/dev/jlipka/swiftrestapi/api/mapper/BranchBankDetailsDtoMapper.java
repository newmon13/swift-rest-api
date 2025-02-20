package dev.jlipka.swiftrestapi.api.mapper;

import dev.jlipka.swiftrestapi.api.dto.BranchBankDetailsDto;
import dev.jlipka.swiftrestapi.domain.model.Bank;
import org.springframework.stereotype.Component;
import java.util.function.Function;
import static java.util.Objects.isNull;

@Component
public class BranchBankDetailsDtoMapper implements Function<Bank, BranchBankDetailsDto>{
    @Override
    public BranchBankDetailsDto apply(Bank bank) {
        return new BranchBankDetailsDto(
                bank.getAddress(),
                bank.getName(),
                bank.getCountryCode(),
                isNull(bank.getHeadquarter()),
                bank.getSwiftCode());
    }
}
