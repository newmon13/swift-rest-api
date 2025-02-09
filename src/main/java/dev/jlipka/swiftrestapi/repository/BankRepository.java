package dev.jlipka.swiftrestapi.repository;

import dev.jlipka.swiftrestapi.model.Bank;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends MongoRepository<Bank, String> {
}
