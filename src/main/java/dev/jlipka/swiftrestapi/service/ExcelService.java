package dev.jlipka.swiftrestapi.service;

import dev.jlipka.swiftrestapi.dto.UploadResponseDto;
import dev.jlipka.swiftrestapi.mapper.EntityExtractor;
import dev.jlipka.swiftrestapi.mapper.ExcelReader;
import dev.jlipka.swiftrestapi.model.Bank;
import dev.jlipka.swiftrestapi.validator.FileValidator;
import dev.jlipka.swiftrestapi.validator.ValidationResult;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;

public class ExcelService<T> {
    @Value("${app.storage.location}")
    private String storageLocation;
    private final FileValidator fileValidator;
    private final ExcelReader excelReader;
    private final EntityExtractor<T> entityExtractor;
    private final EntityService<T> entityService;


    public ExcelService(FileValidator fileValidator,
                        ExcelReader excelReader,
                        EntityExtractor<T> entityExtractor,
                        EntityService<T> entityService) {
        this.fileValidator = fileValidator;
        this.excelReader = excelReader;
        this.entityExtractor = entityExtractor;
        this.entityService = entityService;
    }

    public UploadResponseDto<T> upload(MultipartFile file, boolean hasHeaderRow) {
        ValidationResult validate = fileValidator.validate(file);

        if (validate.result()) {
            try {
                Path uploadPath = Path.of(storageLocation);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(Objects.requireNonNull(file.getOriginalFilename()));
                file.transferTo(new File(String.valueOf(filePath)));

                List<T> entities = getEntities(file.getOriginalFilename(), hasHeaderRow);


                return new UploadResponseDto<>(validate, entities);
            } catch (IOException e) {
                return new UploadResponseDto<>(new ValidationResult(false, "I/O exception occurred"), emptyList());
            }
        } else {
            return new UploadResponseDto<>(new ValidationResult(false, "Validation failed: " + validate.message()), emptyList());
        }
    }

    private List<T> getEntities(String fileName, boolean hasHeaderRow) {
        List<Sheet> sheets = excelReader.getSheets(fileName);
        List<T> entities = new ArrayList<>();

        for (Sheet sheet: sheets) {
            List<T> extract = entityExtractor.extract(sheet, hasHeaderRow);
            entities.addAll(extract);
        }

        return entities;
    }


}
