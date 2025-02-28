package dev.jlipka.swiftrestapi.infrastructure.config;

import dev.jlipka.swiftrestapi.api.mapper.entity.BankMapper;
import dev.jlipka.swiftrestapi.domain.logic.EntityExtractor;
import dev.jlipka.swiftrestapi.domain.logic.ExcelReader;
import dev.jlipka.swiftrestapi.api.mapper.entity.RowMapper;
import dev.jlipka.swiftrestapi.domain.model.Bank;
import dev.jlipka.swiftrestapi.service.EntityService;
import dev.jlipka.swiftrestapi.service.ExcelService;
import dev.jlipka.swiftrestapi.domain.logic.ExcelServiceFactory;
import dev.jlipka.swiftrestapi.api.validator.SpreadsheetFileValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExcelConfiguration {
    @Bean
    public EntityExtractor<Bank> bankEntityExtractor(RowMapper<Bank> rowMapper) {
        return new EntityExtractor<>(rowMapper);
    }

    @Bean
    public ExcelService<Bank> bankExcelService(ExcelServiceFactory excelServiceFactory,
                                               EntityExtractor<Bank> bankEntityExtractor,
                                               EntityService<Bank> bankEntityService,
                                               ExcelReader excelReader,
                                               SpreadsheetFileValidator spreadsheetFileValidator) {
        return excelServiceFactory.createService(bankEntityExtractor, bankEntityService, excelReader, spreadsheetFileValidator);
    }

    @Bean
    RowMapper<Bank> rowMapper() {
        return new BankMapper();
    }
}