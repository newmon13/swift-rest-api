package dev.jlipka.swiftrestapi.repository;

import dev.jlipka.swiftrestapi.dto.BankWithBranchesResponseDto;
import dev.jlipka.swiftrestapi.model.Bank;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankRepository extends MongoRepository<Bank, String> {

    Optional<Bank> findBySwiftCode(String swiftCode);

    List<Bank> getAllByHeadquarter(Bank headquarter);
}
