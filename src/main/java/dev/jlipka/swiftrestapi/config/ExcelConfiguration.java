package dev.jlipka.swiftrestapi.config;

import dev.jlipka.swiftrestapi.mapper.BankMapper;
import dev.jlipka.swiftrestapi.mapper.EntityExtractor;
import dev.jlipka.swiftrestapi.mapper.ExcelReader;
import dev.jlipka.swiftrestapi.mapper.RowMapper;
import dev.jlipka.swiftrestapi.model.Bank;
import dev.jlipka.swiftrestapi.repository.BankRepository;
import dev.jlipka.swiftrestapi.service.BankEntityService;
import dev.jlipka.swiftrestapi.service.EntityService;
import dev.jlipka.swiftrestapi.service.ExcelService;
import dev.jlipka.swiftrestapi.service.ExcelServiceFactory;
import dev.jlipka.swiftrestapi.validator.FileValidator;
import dev.jlipka.swiftrestapi.validator.XlsxValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.Repository;

@Configuration
public class ExcelConfiguration {
    @Bean
    public EntityExtractor<Bank> bankEntityExtractor(RowMapper<Bank> rowMapper) {
        return new EntityExtractor<>(rowMapper);
    }

    @Bean
    public EntityService<Bank> bankEntityService(BankRepository bankRepository) {
        return new BankEntityService(bankRepository);
    }

    @Bean
    public ExcelService<Bank> bankExcelService(ExcelServiceFactory excelServiceFactory,
                                               EntityExtractor<Bank> bankEntityExtractor,
                                               EntityService<Bank> bankEntityService,
                                               ExcelReader excelReader) {
        return excelServiceFactory.createService(bankEntityExtractor, bankEntityService, excelReader);
    }

    @Bean
    FileValidator fileValidator() {
        return new XlsxValidator();
    }

    @Bean
    RowMapper<Bank> rowMapper() {
        return new BankMapper();
    }
}