package dev.jlipka.swiftrestapi.mapper;

import dev.jlipka.swiftrestapi.dto.BankDetailsResponseDto;
import dev.jlipka.swiftrestapi.dto.BankWithBranchesResponseDto;
import dev.jlipka.swiftrestapi.model.Bank;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static java.util.Objects.nonNull;

@Component
public class BankWithBranchesResponseDtoMapper implements BiFunction<Bank, List<Bank>, BankWithBranchesResponseDto> {
    private final BankDetailsResponseDtoMapper mapper;

    public BankWithBranchesResponseDtoMapper(BankDetailsResponseDtoMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public BankWithBranchesResponseDto apply(Bank bank, List<Bank> branches) {
        List<BankDetailsResponseDto> branchDtoList = branches.stream()
                .map(mapper)
                .toList();

        return new BankWithBranchesResponseDto(
                bank.getAddress(),
                bank.getName(),
                bank.getCountryCode(),
                bank.getCountryName(),
                nonNull(bank.getHeadquarter()),
                bank.getSwiftCode(),
                branchDtoList);
    }
}
