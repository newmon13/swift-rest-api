package dev.jlipka.swiftrestapi.repository;

import dev.jlipka.swiftrestapi.domain.model.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataMongoTest
class BankRepositoryTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongo = new MongoDBContainer("mongo:7.0");

    @Autowired
    BankRepository bankRepository;

    @Test
    void shouldEstablishConnection() {
        assertThat(mongo.isCreated()).isTrue();
        assertThat(mongo.isRunning()).isTrue();
    }

    @BeforeEach
    void setUp() {
        List<Bank> banks = List.of(new Bank("AAAAAAAAAAA", "PL", "BIC11", "test name", "test address", "test town name", "test country name", "test time zone", null));
        bankRepository.saveAll(banks);
    }

    @Test
    void shouldReturnBankWithPL() {
        List<Bank> pl = bankRepository.findByCountryCode("PL");
        assertThat(pl).isNotEmpty();
    }
}