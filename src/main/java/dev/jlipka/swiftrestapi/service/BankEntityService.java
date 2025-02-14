package dev.jlipka.swiftrestapi.service;

import dev.jlipka.swiftrestapi.dto.BankWithBranchesDto;
import dev.jlipka.swiftrestapi.model.Bank;
import dev.jlipka.swiftrestapi.repository.BankRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankEntityService implements EntityService<Bank> {
    private final BankRepository bankRepository;

    public BankEntityService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }


    public BankWithBranchesDto findBySwiftCode(String swiftCode) {
        List<Bank> bySwiftCode = bankRepository.findBySwiftCode(swiftCode);
        return new BankWithBranchesDto()
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