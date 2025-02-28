package dev.jlipka.swiftrestapi.domain.logic;

import dev.jlipka.swiftrestapi.api.validator.SpreadsheetFileValidator;
import dev.jlipka.swiftrestapi.service.EntityService;
import dev.jlipka.swiftrestapi.service.ExcelService;
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