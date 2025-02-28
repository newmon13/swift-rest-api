package dev.jlipka.swiftrestapi.api.mapper.entity;

import org.apache.poi.ss.usermodel.Cell;

import java.util.Iterator;

public interface RowMapper<T> {
    T from(Iterator<Cell> rowCells);
}
