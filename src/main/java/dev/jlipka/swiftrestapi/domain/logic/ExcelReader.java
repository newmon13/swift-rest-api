package dev.jlipka.swiftrestapi.domain.logic;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ExcelReader {

    private final String storageLocation;

    public ExcelReader(@Value("${app.storage.location}") String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public List<Sheet> getSheets(String fileName) {
        try (InputStream inputStream = getExcelInputStream(fileName)) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            List<Sheet> sheets = new ArrayList<>();
            workbook.sheetIterator()
                    .forEachRemaining(sheets::add);
            return sheets;
        } catch (IOException e) {
            log.error("I/O exception occurred while trying to read a file", e);
            throw new RuntimeException("Error reading file: " + fileName, e);
        }
    }

    private InputStream getExcelInputStream(String fileName){
        Path storagePath = Path.of(storageLocation);
        Path spreadSheetPath = storagePath.resolve(fileName);
        try {
            return new FileInputStream(spreadSheetPath.toFile());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}