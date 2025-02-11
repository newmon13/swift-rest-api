package dev.jlipka.swiftrestapi.mapper;

import dev.jlipka.swiftrestapi.validator.FileValidator;
import dev.jlipka.swiftrestapi.validator.ValidationResult;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;
import java.util.stream.StreamSupport;

public class EntityExtractor<T> {
    private final FileValidator fileValidator;
    private final RowMapper<T> mapperClass;
    private final SpreadsheetReader spreadsheetReader;
    private boolean hasHeaders;

    public EntityExtractor(FileValidator fileValidator,
                           RowMapper<T> mapperClass,
                           SpreadsheetReader spreadsheetReader) {
        this.fileValidator = fileValidator;
        this.mapperClass = mapperClass;
        this.spreadsheetReader = spreadsheetReader;
    }

    public List<T> extract(boolean hasHeaders) {
        this.hasHeaders = hasHeaders;
        List<T> entities = new ArrayList<>();
        try {
            File file = spreadsheetReader.getFile();
            if (validateFile(file).result()) {
                List<Sheet> sheets = spreadsheetReader.getSheets();
                for (Sheet sheet : sheets) {
                    entities.addAll(extractSheet(sheet));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return entities;
    }

    private ValidationResult validateFile(File file) {
        return fileValidator.validate(file);
    }

    private List<T> extractSheet(Sheet sheet) {
        List<T> entities = new ArrayList<>();
        Iterator<Row> rowIterator = eliminateBlankRows(sheet);
        if (hasHeaders) {
            skipHeaders(rowIterator);
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            T entity = mapperClass.mapRowToEntity(cellIterator);
            System.out.println(entity);
            entities.add(entity);
        }
        return entities;
    }

    private Iterator<Row> eliminateBlankRows(Sheet sheet) {
        return StreamSupport.stream(sheet.spliterator(), true)
                .filter(row -> StreamSupport.stream(row.spliterator(), false)
                        .anyMatch(cell -> cell != null &&
                                cell.getCellType() != CellType.BLANK &&
                                !cell.getStringCellValue()
                                        .equals("<null>")))
                .iterator();
    }

    private void skipHeaders(Iterator<Row> rowIterator) {
        rowIterator.next();
    }
}
