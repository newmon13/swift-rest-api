package dev.jlipka.swiftrestapi.service;

import dev.jlipka.swiftrestapi.mapper.EntityExtractor;
import dev.jlipka.swiftrestapi.mapper.ExcelReader;
import dev.jlipka.swiftrestapi.validator.SpreadsheetFileValidator;
import org.springframework.stereotype.Component;

@Component
public class ExcelServiceFactory {

    public <T> ExcelService<T> createService(EntityExtractor<T> entityExtractor,
                                             EntityService<T> entityService,
                                             ExcelReader excelReader,
                                             SpreadsheetFileValidator spreadsheetFileValidator
    ) {
        return new ExcelService<>(spreadsheetFileValidator, excelReader, entityExtractor, entityService);
    }
}