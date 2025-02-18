package dev.jlipka.swiftrestapi.service;

import dev.jlipka.swiftrestapi.api.dto.UploadResponseDto;
import dev.jlipka.swiftrestapi.domain.logic.EntityExtractor;
import dev.jlipka.swiftrestapi.infrastructure.error.FileUploadException;
import dev.jlipka.swiftrestapi.infrastructure.error.ValidationException;
import dev.jlipka.swiftrestapi.domain.logic.ExcelReader;
import dev.jlipka.swiftrestapi.api.validator.SpreadsheetFileValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Slf4j
public class ExcelService<T> {
    @Value("${app.storage.location}")
    private String storageLocation;
    private final SpreadsheetFileValidator fileValidator;
    private final ExcelReader excelReader;
    private final EntityExtractor<T> entityExtractor;
    private final EntityService<T> entityService;

    public ExcelService(SpreadsheetFileValidator fileValidator,
                        ExcelReader excelReader,
                        EntityExtractor<T> entityExtractor,
                        EntityService<T> entityService) {
        this.fileValidator = fileValidator;
        this.excelReader = excelReader;
        this.entityExtractor = entityExtractor;
        this.entityService = entityService;
    }

    @Transactional
    public UploadResponseDto<T> importFile(MultipartFile file, boolean hasHeaderRow) {
        validateFile(file);
        Path filePath = null;
        try {
            filePath = Path.of(saveFile(file));
            List<T> entities = getEntities(filePath.toString(), hasHeaderRow);
            List<T> savedEntities = persistEntities(entities);
            return new UploadResponseDto<>("Successfully uploaded and persisted entities from given file",
                    entities.size(),
                    savedEntities.size());
        } catch (IOException e) {
            throw new FileUploadException("Failed to upload file: " + e.getMessage());
        } finally {
            cleanupFile(filePath);
        }
    }

    private void validateFile(MultipartFile file) {
        if (!fileValidator.supports(file.getClass())) {
            throw new ValidationException("File type not supported");
        }

        Errors errors = new BeanPropertyBindingResult(file, "file");
        fileValidator.validate(file, errors);

        if (errors.hasErrors()) {
            throw new ValidationException(errors.getAllErrors());
        }
    }

    private String saveFile(MultipartFile file) throws IOException {
        Path uploadPath = Path.of(storageLocation);
        Files.createDirectories(uploadPath);

        Path filePath = uploadPath.resolve(Objects.requireNonNull(file.getOriginalFilename()));
        file.transferTo(filePath.toFile());

        return filePath.toString();
    }

    private List<T> persistEntities(List<T> entities) {
        return entities.stream()
                .map(entityService::save)
                .toList();
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

    private void cleanupFile(Path filePath) {
        if (filePath != null) {
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
