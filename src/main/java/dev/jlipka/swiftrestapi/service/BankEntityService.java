package dev.jlipka.swiftrestapi.service;

import dev.jlipka.swiftrestapi.api.dto.Branch;
import dev.jlipka.swiftrestapi.api.dto.CountrySwiftCodes;
import dev.jlipka.swiftrestapi.api.dto.FailedImport;
import dev.jlipka.swiftrestapi.api.dto.Headquarter;
import dev.jlipka.swiftrestapi.api.mapper.dto.BankDtoMapper;
import dev.jlipka.swiftrestapi.api.validator.BankValidator;
import dev.jlipka.swiftrestapi.domain.model.Bank;
import dev.jlipka.swiftrestapi.domain.model.BankType;
import dev.jlipka.swiftrestapi.infrastructure.error.BankNotFoundException;
import dev.jlipka.swiftrestapi.infrastructure.error.DuplicateResourceException;
import dev.jlipka.swiftrestapi.infrastructure.error.ValidationException;
import dev.jlipka.swiftrestapi.repository.BankRepository;
import org.apache.commons.lang3.function.TriFunction;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;

import static java.util.Collections.emptyList;
import static java.util.Locale.of;

@Service
public class BankEntityService implements EntityService<Bank> {

    private static final int DEFAULT_SWIFT_CODE_LENGTH = 8;
    private final BankRepository bankRepository;
    private final BankValidator bankValidator;
    private final BankDtoMapper bankDtoMapper;

    public BankEntityService(BankRepository bankRepository, BankValidator bankValidator, BankDtoMapper bankDtoMapper) {
        this.bankRepository = bankRepository;
        this.bankValidator = bankValidator;
        this.bankDtoMapper = bankDtoMapper;
    }

    public CountrySwiftCodes findByCountryCode(String countryCode) {
        validate(countryCode, "countryName", bankValidator.getCountryCodeValidator());

        List<Bank> foundBanks = bankRepository.findByCountryCode(countryCode);
        String countryName = getCountryName(countryCode);
        TriFunction<String, String, List<Bank>, CountrySwiftCodes> countrySwiftCodesMapFunction = bankDtoMapper.toCountrySwiftCodes();

        return countrySwiftCodesMapFunction.apply(countryCode, countryName, foundBanks);
    }

    private String getCountryName(String countryCode) {
        Locale locale = of("", countryCode);
        return locale.getDisplayCountry();
    }

    public Branch findBySwiftCode(String swiftCode) {
        validate(swiftCode, "swiftCode", bankValidator.getSwiftCodeValidator());

        Bank foundBank = bankRepository.findBySwiftCode(swiftCode)
                .orElseThrow(() -> new BankNotFoundException("Bank not found for SWIFT code: " + swiftCode));

        BankType bankType = getBankType(foundBank.getSwiftCode());

        if (bankType == BankType.HEADQUARTER) {
            BiFunction<Bank, List<Bank>, Headquarter> headquarterMapperFunction = bankDtoMapper.toHeadquarter();
            return headquarterMapperFunction.apply(foundBank, getBankBranches(foundBank));
        } else {
            Function<Bank, Branch> branchMapperFunction = bankDtoMapper.toBranch();
            return branchMapperFunction.apply(foundBank);
        }
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
        BankType bankType = getBankType(bank.getSwiftCode());
        if (bankType == BankType.HEADQUARTER) {
            return bankRepository.getAllByHeadquarter(bank);
        } else {
            return emptyList();
        }
    }

    public Map<String, String> registerBank(Branch bankFullDetailsDto) {
        Function<Branch, Bank> branchBankFunction = bankDtoMapper.fromBranch();
        Bank mappedBank = branchBankFunction.apply(bankFullDetailsDto);
        Bank validatedBank = validate(mappedBank, "bank", bankValidator);
        checkForDuplicateResource(validatedBank);
        save(validatedBank);
        return Map.of("message", "Bank created successfully");
    }

    private BankType getBankType(String swiftCode) {
        if (swiftCode.endsWith("XXX")) {
            return BankType.HEADQUARTER;
        } else {
            return BankType.BRANCH;
        }
    }

    private void checkForDuplicateResource(Bank bank) {
        if (bankRepository.findBySwiftCode(bank.getSwiftCode())
                .isPresent()) {
            throw new DuplicateResourceException("Bank with code " + bank.getSwiftCode() + " already exists");
        }
    }

    private String getHeadquarterPrefix(String swiftCode) {
        return swiftCode.substring(0, DEFAULT_SWIFT_CODE_LENGTH);
    }

    public Map<String, String> unregister(String swiftCode) {
        Optional<Bank> bank = bankRepository.findBySwiftCode(swiftCode);

        if (bank.isPresent()) {
            Bank foundBank = bank.get();
            BankType bankType = getBankType(foundBank.getSwiftCode());
            if (bankType == BankType.HEADQUARTER) {
                List<Bank> headquarterBranches = getHeadquarterBranches(foundBank);
                setBranchesHeadquarter(headquarterBranches, null);

                if (!headquarterBranches.isEmpty()) {
                    bankRepository.saveAll(headquarterBranches);
                }
            }
            bankRepository.delete(foundBank);
        } else {
            throw new BankNotFoundException("No bank with given swift code found");
        }
        return Map.of("message", "Bank deleted successfully");
    }

    @Override
    public Bank save(Bank bank) throws ValidationException {
        if (!bankValidator.supports(bank.getClass())) {
            throw new IllegalArgumentException("No validator found for entity type");
        }

        Errors errors = new BeanPropertyBindingResult(bank, "bank");
        bankValidator.validate(bank, errors);

        if (errors.hasErrors()) {
            List<String> serializableErrors = errors.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            FailedImport failedImport = new FailedImport(bank, serializableErrors);
            throw new ValidationException(failedImport);
        }

        BankType bankType = getBankType(bank.getSwiftCode());

        if (bankType == BankType.HEADQUARTER) {
            Bank savedHeadquarter = bankRepository.save(bank);
            List<Bank> headquarterBranches = getHeadquarterBranches(savedHeadquarter);
            setBranchesHeadquarter(headquarterBranches, bank);

            if (!headquarterBranches.isEmpty()) {
                bankRepository.saveAll(headquarterBranches);
            }
            return savedHeadquarter;

        } else {
            Optional<Bank> headquarter = getHeadquarter(bank.getSwiftCode());
            Bank bankToSave = assignHeadquarterToBranch(headquarter, bank);
            return bankRepository.save(bankToSave);
        }
    }

    private List<Bank> getHeadquarterBranches(Bank headquarter) {
        String headquarterPrefix = getHeadquarterPrefix(headquarter.getSwiftCode());
        Pattern branchPattern = Pattern.compile("^" + headquarterPrefix + "(?!XXX)");
        return bankRepository.getAllBySwiftCodeMatchesRegex(branchPattern.pattern());
    }

    private void setBranchesHeadquarter(List<Bank> branches, Bank headquarter) {
        branches.forEach(bank -> bank.setHeadquarter(headquarter));
    }

    private Optional<Bank> getHeadquarter(String swiftCode) {
        String headquarterSwiftCode = getHeadquarterPrefix(swiftCode) + "XXX";
        return bankRepository.findBySwiftCode(headquarterSwiftCode);
    }

    private Bank assignHeadquarterToBranch(Optional<Bank> headquarter, Bank branch) {
        headquarter.ifPresent(branch::setHeadquarter);
        return branch;
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