package dev.jlipka.swiftrestapi.mapper;

import org.apache.poi.ss.usermodel.*;

import java.util.*;
import java.util.stream.StreamSupport;

public class    EntityExtractor<T> {
    private final List<Sheet> sheets;
    private final RowMapper<T> rowMapper;
    private boolean hasHeaders;

    public EntityExtractor(List<Sheet> sheets, RowMapper<T> rowMapper) {
        this.sheets = sheets;
        this.rowMapper = rowMapper;
    }

    public List<T> extract(boolean hasHeaders) {
        this.hasHeaders = hasHeaders;
        List<T> entities = new ArrayList<>();
        for (Sheet sheet : sheets) {
            entities.addAll(extractSheet(sheet));
        }
        return entities;
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
            Optional<T> entity = rowMapper.mapRowToEntity(cellIterator);
            entity.ifPresent(entities::add);
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
