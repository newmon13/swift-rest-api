package dev.jlipka.swiftrestapi.config;

import dev.jlipka.swiftrestapi.mapper.BankMapper;
import dev.jlipka.swiftrestapi.mapper.EntityExtractor;
import dev.jlipka.swiftrestapi.mapper.ExcelReader;
import dev.jlipka.swiftrestapi.mapper.RowMapper;
import dev.jlipka.swiftrestapi.model.Bank;
import dev.jlipka.swiftrestapi.service.EntityService;
import dev.jlipka.swiftrestapi.service.ExcelService;
import dev.jlipka.swiftrestapi.service.ExcelServiceFactory;
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
                                               ExcelReader excelReader) {
        return excelServiceFactory.createService(bankEntityExtractor, bankEntityService, excelReader);
    }

    @Bean
    RowMapper<Bank> rowMapper() {
        return new BankMapper();
    }
}