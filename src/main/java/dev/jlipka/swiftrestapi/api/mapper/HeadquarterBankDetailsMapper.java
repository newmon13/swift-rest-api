package dev.jlipka.swiftrestapi.api.mapper;


import dev.jlipka.swiftrestapi.api.dto.BranchBankDetailsDto;
import dev.jlipka.swiftrestapi.api.dto.HeadquarterBankDetailsDto;
import dev.jlipka.swiftrestapi.domain.model.Bank;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

import static java.util.Objects.isNull;

@Component
public class HeadquarterBankDetailsMapper implements BiFunction<Bank, List<Bank>, HeadquarterBankDetailsDto> {
    private final BranchBankDetailsDtoMapper mapper;

    public HeadquarterBankDetailsMapper(BranchBankDetailsDtoMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public HeadquarterBankDetailsDto apply(Bank bank, List<Bank> branches) {
        List<BranchBankDetailsDto> branchDtoList = branches.stream()
                .map(mapper)
                .toList();

        return new HeadquarterBankDetailsDto(
                bank.getAddress(),
                bank.getName(),
                bank.getCountryCode(),
                bank.getCountryName(),
                isNull(bank.getHeadquarter()),
                bank.getSwiftCode(),
                branchDtoList);
    }
}
