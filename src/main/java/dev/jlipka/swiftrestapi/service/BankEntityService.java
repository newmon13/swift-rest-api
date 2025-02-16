package dev.jlipka.swiftrestapi.service;

import dev.jlipka.swiftrestapi.dto.*;
import dev.jlipka.swiftrestapi.error.BankNotFoundException;
import dev.jlipka.swiftrestapi.error.DuplicateResourceException;
import dev.jlipka.swiftrestapi.error.ValidationException;
import dev.jlipka.swiftrestapi.mapper.*;
import dev.jlipka.swiftrestapi.model.Bank;
import dev.jlipka.swiftrestapi.repository.BankRepository;
import dev.jlipka.swiftrestapi.validator.BankValidator;
import dev.jlipka.swiftrestapi.validator.CountryCodeValidator;
import dev.jlipka.swiftrestapi.validator.SwiftCodeValidator;
import org.springframework.stereotype.Service;
import org.springframework.validation.*;

import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Locale.of;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class BankEntityService implements EntityService<Bank> {
    private static final int EXTENDED_SWIFT_CODE_LENGTH = 11;
    private static final int DEFAULT_SWIFT_CODE_LENGTH = 8;

    private final BankRepository bankRepository;
    private final BankValidator bankValidator;
    private final BankMapper bankMapper;
    private final BankDetailsResponseDtoMapper bankDetailsMapper;
    private final BankFullResponseDtoMapper bankFullMapper;
    private final BankWithBranchesResponseDtoMapper bankWithBranchesMapper;
    private final CountryWithBanksResponseDtoMapper countryWithBanksMapper;
    private final CountryCodeValidator countryCodeValidator;
    private final SwiftCodeValidator swiftCodeValidator;

    public BankEntityService(BankRepository bankRepository, BankValidator bankValidator, BankMapper bankMapper,
                             BankDetailsResponseDtoMapper bankDetailsMapper,
                             BankFullResponseDtoMapper bankFullMapper,
                             BankWithBranchesResponseDtoMapper bankWithBranchesMapper,
                             CountryWithBanksResponseDtoMapper countryWithBanksMapper, CountryCodeValidator countryCodeValidator, SwiftCodeValidator swiftCodeValidator) {
        this.bankRepository = bankRepository;
        this.bankValidator = bankValidator;
        this.bankMapper = bankMapper;
        this.bankDetailsMapper = bankDetailsMapper;
        this.bankFullMapper = bankFullMapper;
        this.bankWithBranchesMapper = bankWithBranchesMapper;
        this.countryWithBanksMapper = countryWithBanksMapper;
        this.countryCodeValidator = countryCodeValidator;
        this.swiftCodeValidator = swiftCodeValidator;
    }

    public CountryWithBanksResponseDto findByCountryCode(String countryCode) {
        validate(countryCode, "countryCode", countryCodeValidator);

        List<Bank> foundBanks = bankRepository.findByCountryCode(countryCode);
        String countryName = getCountryName(countryCode);
        return countryWithBanksMapper.apply(countryCode, countryName, foundBanks);
    }

    private String getCountryName(String countryCode) {
        Locale locale = of("", countryCode);
        return locale.getDisplayCountry();
    }

    public BankWithBranchesResponseDto findBySwiftCode(String swiftCode) {
        validate(swiftCode, "swiftCode", swiftCodeValidator);

        Bank foundBank = bankRepository.findBySwiftCode(swiftCode)
                .orElseThrow(() -> new BankNotFoundException("Bank not found for SWIFT code: " + swiftCode));
        return bankWithBranchesMapper.apply(foundBank, getBankBranches(foundBank));
    }

    private <T> T validate(T object, String objectName, Validator validator) {
        Errors errors = new BeanPropertyBindingResult(object, objectName);
        if (validator.supports(object.getClass())) {
            validator.validate(object, errors);
        }
        checkForValidationErrors(errors);
        return object;
    }

    private void checkForValidationErrors(Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException(errors.getAllErrors());
        }
    }

    private List<Bank> getBankBranches(Bank bank) {
        if (isNull(bank.getHeadquarter())) {
            return bankRepository.getAllByHeadquarter(bank);
        } else {
            return emptyList();
        }
    }

    public CrudOperationResponseDto registerBank(BankFullDetailsDto bankFullDetailsDto) {
        Bank mappedBank = bankMapper.from(bankFullDetailsDto);
        Bank validatedBank = validate(mappedBank, "bank", bankValidator);
        checkForDuplicateResource(validatedBank);

        Optional<Bank> headquarter = getHeadquarter(validatedBank.getSwiftCode());
        Bank bankToSave = assignHeadquarterToBranch(headquarter, validatedBank);
        bankRepository.save(bankToSave);
        return new CrudOperationResponseDto("Bank created successfully");
    }

    private void checkForDuplicateResource(Bank bank) {
        if (bankRepository.findBySwiftCode(bank.getSwiftCode()).isPresent()) {
            throw new DuplicateResourceException("Bank with code " + bank.getSwiftCode() + " already exists");
        }
    }

    private Optional<Bank> getHeadquarter(String swiftCode) {
        int swiftCodeLength = swiftCode.length();
        if (swiftCodeLength == EXTENDED_SWIFT_CODE_LENGTH) {
            String headquarterSwiftCode = getHeadquarterCode(swiftCode);
            return bankRepository.findBySwiftCode(headquarterSwiftCode);
        } else if (swiftCodeLength == DEFAULT_SWIFT_CODE_LENGTH) {
            return bankRepository.findBySwiftCode(swiftCode);
        } else {
            return Optional.empty();
        }
    }
    private String getHeadquarterCode(String swiftCode) {
        return swiftCode.substring(0, DEFAULT_SWIFT_CODE_LENGTH + 1);
    }

    private Bank assignHeadquarterToBranch(Optional<Bank> headquarter, Bank branch) {
        headquarter.ifPresent(branch::setHeadquarter);
        return branch;
    }

    public CrudOperationResponseDto unregister(String swiftCode) {
        Optional<Bank> bank = bankRepository.findBySwiftCode(swiftCode);

        if (bank.isPresent()) {
            bankRepository.deleteBySwiftCode(swiftCode);
        } else {
            throw new BankNotFoundException("No bank with given swift code found");
        }
        return new CrudOperationResponseDto("Bank deleted successfully");
    }

    @Override
    public Bank save(Bank entity) {
        if (bankValidator.supports(entity.getClass())) {
            Errors errors = new BeanPropertyBindingResult(entity, "bank");
            bankValidator.validate(entity, errors);

            if (errors.hasErrors()) {
                throw new ValidationException(errors.getAllErrors());
            }
        }
        return bankRepository.save(entity);
    }

    @Override
    public Optional<Bank> findById(String id) {
        return bankRepository.findById(id);
    }

    @Override
    public List<Bank> findAll() {
        return bankRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        bankRepository.deleteById(id);
    }
}