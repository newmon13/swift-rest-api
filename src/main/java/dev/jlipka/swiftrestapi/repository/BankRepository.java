package dev.jlipka.swiftrestapi.repository;

import dev.jlipka.swiftrestapi.domain.model.Bank;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankRepository extends MongoRepository<Bank, String> {

    Optional<Bank> findBySwiftCode(String swiftCode);

    List<Bank> getAllByHeadquarter(Bank headquarter);

    List<Bank> findByCountryCode(String countryCode);

    void deleteBySwiftCode(String swiftCode);

    List<Bank> getAllBySwiftCodeStartingWith(String swiftCode);
}
