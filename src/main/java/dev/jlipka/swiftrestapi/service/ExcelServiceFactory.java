package dev.jlipka.swiftrestapi.service;

import dev.jlipka.swiftrestapi.mapper.EntityExtractor;
import dev.jlipka.swiftrestapi.mapper.ExcelReader;
import dev.jlipka.swiftrestapi.validator.XlsxValidator;
import org.springframework.stereotype.Component;

@Component
public class ExcelServiceFactory {

    public <T> ExcelService<T> createService(EntityExtractor<T> entityExtractor,
                                             EntityService<T> entityService,
                                             ExcelReader excelReader,
                                             XlsxValidator xlsxValidator
    ) {
        return new ExcelService<>(xlsxValidator, excelReader, entityExtractor, entityService);
    }
}