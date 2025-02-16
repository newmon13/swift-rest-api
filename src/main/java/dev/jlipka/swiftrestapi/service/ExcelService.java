package dev.jlipka.swiftrestapi.service;

import dev.jlipka.swiftrestapi.dto.UploadResponseDto;
import dev.jlipka.swiftrestapi.error.FileUploadException;
import dev.jlipka.swiftrestapi.error.ValidationException;
import dev.jlipka.swiftrestapi.mapper.EntityExtractor;
import dev.jlipka.swiftrestapi.mapper.ExcelReader;
import dev.jlipka.swiftrestapi.validator.XlsxValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Value;
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
    private final XlsxValidator fileValidator;
    private final ExcelReader excelReader;
    private final EntityExtractor<T> entityExtractor;
    private final EntityService<T> entityService;


    public ExcelService(XlsxValidator fileValidator,
                        ExcelReader excelReader,
                        EntityExtractor<T> entityExtractor,
                        EntityService<T> entityService) {
        this.fileValidator = fileValidator;
        this.excelReader = excelReader;
        this.entityExtractor = entityExtractor;
        this.entityService = entityService;
    }

    public UploadResponseDto<T> upload(MultipartFile file, boolean hasHeaderRow) {
        validateFile(file);

        try {
            String savedFilePath = saveFile(file);
            List<T> entities = getEntities(savedFilePath, hasHeaderRow);
            persistEntities(entities);

            return new UploadResponseDto<>("Successfully uploaded and persisted entities from given .xlsx file", entities);
        } catch (IOException e) {
            throw new FileUploadException("Failed to upload file");
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

    private void persistEntities(List<T> entities) {
        log.info("Persisting uploaded entities");
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
