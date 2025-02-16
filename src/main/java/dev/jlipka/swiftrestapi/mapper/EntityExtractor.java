package dev.jlipka.swiftrestapi.mapper;

import org.apache.poi.ss.usermodel.*;

import java.util.*;
import java.util.stream.StreamSupport;

public class EntityExtractor<T> {
    private final RowMapper<T> rowMapper;

    public EntityExtractor(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    public List<T> extract(Sheet sheet, boolean hasHeaderRow) {
        return new ArrayList<>(extractSheet(sheet, hasHeaderRow));
    }

    private List<T> extractSheet(Sheet sheet, boolean hasHeaderRow) {
        List<T> entities = new ArrayList<>();
        Iterator<Row> rowIterator = eliminateBlankRows(sheet);
        if (hasHeaderRow) {
            skipHeaderRow(rowIterator);
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            T entity = rowMapper.from(cellIterator);
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

    private void skipHeaderRow(Iterator<Row> rowIterator) {
        rowIterator.next();
    }
}
