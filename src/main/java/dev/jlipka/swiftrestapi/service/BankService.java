package dev.jlipka.swiftrestapi.service;

import dev.jlipka.swiftrestapi.dto.UploadResponseDto;
import dev.jlipka.swiftrestapi.mapper.BankMapper;
import dev.jlipka.swiftrestapi.mapper.ClassPathResourceSpreadsheetReader;
import dev.jlipka.swiftrestapi.mapper.EntityExtractor;
import dev.jlipka.swiftrestapi.model.Bank;
import dev.jlipka.swiftrestapi.validator.XlsxValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class BankService {
    private EntityExtractor<Bank> entityExtractor;
    private FileManager fileManager;

    public BankService() {}

    public ResponseEntity<UploadResponseDto> processFile(File file) {
        return null;
    }
}
