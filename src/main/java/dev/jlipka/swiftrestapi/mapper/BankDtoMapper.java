package dev.jlipka.swiftrestapi.mapper;

import dev.jlipka.swiftrestapi.dto.BankDetailsResponseDto;
import dev.jlipka.swiftrestapi.dto.BankFullDetailsDto;
import dev.jlipka.swiftrestapi.dto.BankWithBranchesResponseDto;
import dev.jlipka.swiftrestapi.dto.CountryWithBanksResponseDto;
import dev.jlipka.swiftrestapi.model.Bank;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BankDtoMapper {

    public BankDetailsResponseDto mapToBankDetailsResponseDto(Bank bank) {
        return new BankDetailsResponseDto(
                bank.getAddress(),
                bank.getName(),
                bank.getCountryCode(),
                isHeadquarter(bank),
                bank.getSwiftCode());
    }

    public BankFullDetailsDto mapToBankFullDetailsDto(Bank bank) {
        return new BankFullDetailsDto(
                bank.getAddress(),
                bank.getName(),
                bank.getCountryCode(),
                bank.getCountryName(),
                isHeadquarter(bank),
                bank.getSwiftCode());
    }


    public BankWithBranchesResponseDto mapToBankWithBranchesResponseDto(Bank bank, List<Bank> branches) {
        List<BankDetailsResponseDto> bankDetailsResponseDtoList = new ArrayList<>();

        for (Bank branch: branches) {
            bankDetailsResponseDtoList.add(mapToBankDetailsResponseDto(branch));
        }

        return new BankWithBranchesResponseDto(
                bank.getAddress(),
                bank.getName(),
                bank.getCountryCode(),
                bank.getCountryName(),
                isHeadquarter(bank),
                bank.getSwiftCode(),
                bankDetailsResponseDtoList);
    }

    public CountryWithBanksResponseDto mapToCountryWithBanksResponseDto(String countryCode, String countryName, List<Bank> banks) {
        List<BankDetailsResponseDto> bankDetailsResponseDtoList = new ArrayList<>();

        for (Bank bank: banks) {
            bankDetailsResponseDtoList.add(mapToBankDetailsResponseDto(bank));
        }

        return new CountryWithBanksResponseDto(
                countryCode,
                countryName,
                bankDetailsResponseDtoList);
    }


    private boolean isHeadquarter(Bank bank) {
        return Objects.isNull(bank.getHeadquarter());
    }
}
