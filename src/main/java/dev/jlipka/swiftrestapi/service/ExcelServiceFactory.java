package dev.jlipka.swiftrestapi.service;

import dev.jlipka.swiftrestapi.mapper.EntityExtractor;
import dev.jlipka.swiftrestapi.mapper.ExcelReader;
import dev.jlipka.swiftrestapi.validator.FileValidator;
import org.springframework.stereotype.Component;

@Component
public class ExcelServiceFactory {
    private final FileValidator fileValidator;

    public ExcelServiceFactory(FileValidator fileValidator) {
        this.fileValidator = fileValidator;
    }

    public <T> ExcelService<T> createService(EntityExtractor<T> entityExtractor,
                                             EntityService<T> entityService,
                                             ExcelReader excelReader) {
        return new ExcelService<>(fileValidator, excelReader, entityExtractor, entityService);
    }
}