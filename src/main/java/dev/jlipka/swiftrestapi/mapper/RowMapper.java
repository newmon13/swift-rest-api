package dev.jlipka.swiftrestapi.mapper;

import org.apache.poi.ss.usermodel.Cell;

import java.util.Iterator;

public interface RowMapper<T> {
    T mapRowToEntity(Iterator<Cell> rowCells);
}
