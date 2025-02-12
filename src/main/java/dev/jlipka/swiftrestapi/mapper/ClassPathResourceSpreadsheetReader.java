package dev.jlipka.swiftrestapi.mapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ClassPathResourceSpreadsheetReader {
    private ClassPathResource classPathResource;

    public void setResourceFile(String fileName) {
        classPathResource = new ClassPathResource(fileName);
    }

    public File getResourceFile() {
        try {
            return classPathResource.getFile();
        } catch (IOException e) {
            throw new IllegalStateException("Class path resource file is not set");
        }
    }

    public List<Sheet> getSheets() {
        try (InputStream inputStream = classPathResource.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            List<Sheet> sheets = new ArrayList<>();
            workbook.sheetIterator()
                    .forEachRemaining(sheets::add);
            return sheets;

        } catch (IOException e) {
            log.error("Error processing file: {}", classPathResource.getFilename(), e);
            throw new RuntimeException("Could not process file: " + classPathResource.getFilename()
                    + ". Class path resource file is not set?", e);
        }
    }
}