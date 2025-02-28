package dev.jlipka.swiftrestapi.api.mapper.dto;

import dev.jlipka.swiftrestapi.api.dto.Branch;
import dev.jlipka.swiftrestapi.domain.model.Bank;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class BranchToBankMapper implements Function<Branch, Bank> {
    @Override
    public Bank apply(Branch branch) {
        return Bank.builder()
                .address(branch.getAddress())
                .name(branch.getBankName())
                .swiftCode(branch.getSwiftCode())
                .countryCode(branch.getCountryISO2())
                .countryName(branch.getCountryName())
                .build();
    }
}