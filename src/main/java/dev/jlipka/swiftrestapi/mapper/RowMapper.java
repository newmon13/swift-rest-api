package dev.jlipka.swiftrestapi.mapper;

import org.apache.poi.ss.usermodel.Cell;

import java.util.Iterator;
import java.util.Optional;

public interface RowMapper<T> {
    T from(Iterator<Cell> rowCells);
}
