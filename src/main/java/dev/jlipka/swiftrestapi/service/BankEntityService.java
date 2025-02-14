package dev.jlipka.swiftrestapi.service;

import dev.jlipka.swiftrestapi.dto.BankFullDetailsDto;
import dev.jlipka.swiftrestapi.dto.BankRegistrationResponseDto;
import dev.jlipka.swiftrestapi.dto.BankWithBranchesResponseDto;
import dev.jlipka.swiftrestapi.model.Bank;
import dev.jlipka.swiftrestapi.repository.BankRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankEntityService implements EntityService<Bank> {
    private static final int EXTENDED_SWIFT_CODE_LENGTH = 11;
    private static final int DEFAULT_SWIFT_CODE_LENGTH = 8;
    private final BankRepository bankRepository;
    privaate

    public BankEntityService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }


    public BankWithBranchesResponseDto findBySwiftCode(String swiftCode) {
        return new BankWithBranchesResponseDto(bankRepository.findBySwiftCode(swiftCode).);
    }


    public BankRegistrationResponseDto registerBank(BankFullDetailsDto bankFullDetailsDto) {


    }

    private boolean isHeadquarter(String swiftCode)  {
        return swiftCode.strip().endsWith("XXX");
    }

    private Optional<Bank> getHeadquarter(String swiftCode) {
        int swiftCodeLength = swiftCode.length();
        if (swiftCodeLength == EXTENDED_SWIFT_CODE_LENGTH) {
            String headquarterSwiftCode = getHeadquarterCode(swiftCode);
        } else if (swiftCodeLength == DEFAULT_SWIFT_CODE_LENGTH) {
            return bankRepository.findBySwiftCode()
        }
        bankRepository.findBySwiftCodeM
    }


    private String getHeadquarterCode(String swiftCode) {
        return swiftCode.substring(0, DEFAULT_SWIFT_CODE_LENGTH + 1);
    }


    @Override
    public Bank save(Bank entity) {
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