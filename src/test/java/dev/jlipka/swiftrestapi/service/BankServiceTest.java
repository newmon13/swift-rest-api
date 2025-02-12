package dev.jlipka.swiftrestapi.service;

import dev.jlipka.swiftrestapi.dto.UploadResponseDto;
import dev.jlipka.swiftrestapi.model.Bank;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class BankServiceTest {
    private BankService bankService = new BankService();


}