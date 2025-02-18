package dev.jlipka.swiftrestapi.service;

import dev.jlipka.swiftrestapi.domain.logic.EntityExtractor;
import dev.jlipka.swiftrestapi.domain.logic.ExcelReader;
import dev.jlipka.swiftrestapi.api.validator.SpreadsheetFileValidator;
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