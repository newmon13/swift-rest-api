package dev.jlipka.swiftrestapi.mapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class SpreadsheetReader extends ClassPathResource {

    public SpreadsheetReader(String path) {
        super(path);
    }

    public List<Sheet> getSheets() {
        List<Sheet> sheets = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(super.getFile()))) {
            workbook.sheetIterator().forEachRemaining(sheets::add);
        } catch (IOException e) {
            throw new RuntimeException("Could not process file: ", e);
        }
        return sheets;
    }
}
