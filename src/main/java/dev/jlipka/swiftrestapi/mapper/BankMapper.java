package dev.jlipka.swiftrestapi.mapper;

import dev.jlipka.swiftrestapi.error.InvalidBankDataException;
import dev.jlipka.swiftrestapi.model.Bank;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;

import java.util.*;

@Slf4j
public class BankMapper implements RowMapper<Bank> {
    private enum BankColumn {
        COUNTRY_CODE(0),
        SWIFT_CODE(1),
        CODE_TYPE(2),
        NAME(3),
        ADDRESS(4),
        TOWN_NAME(5),
        COUNTRY_NAME(6),
        TIME_ZONE(7);

        private final int index;

        BankColumn(int index) {
            this.index = index;
        }
    }

    @Override
    public Optional<Bank> mapRowToEntity(Iterator<Cell> rowCells) {
        Map<Integer, String> cellValues = extractCellValues(rowCells);
        if (validateRequiredFields(cellValues)) {
            return Optional.of(Bank.builder()
                    .countryCode(getCellValue(cellValues, BankColumn.COUNTRY_CODE))
                    .swiftCode(getCellValue(cellValues, BankColumn.SWIFT_CODE))
                    .codeType(getCellValue(cellValues, BankColumn.CODE_TYPE))
                    .name(getCellValue(cellValues, BankColumn.NAME))
                    .address(getCellValue(cellValues, BankColumn.ADDRESS))
                    .townName(getCellValue(cellValues, BankColumn.TOWN_NAME))
                    .countryName(getCellValue(cellValues, BankColumn.COUNTRY_NAME))
                    .timeZone(getCellValue(cellValues, BankColumn.TIME_ZONE))
                    .build());
        }
        return Optional.empty();
    }

    private Map<Integer, String> extractCellValues(Iterator<Cell> rowCells) {
        Map<Integer, String> cellValues = new HashMap<>();

        while (rowCells.hasNext()) {
            Cell cell = rowCells.next();
            cellValues.put(cell.getColumnIndex(), cell.getStringCellValue());
        }

        return cellValues;
    }

    private String getCellValue(Map<Integer, String> cellValues, BankColumn column) {
        String value = cellValues.get(column.index);
        if (value == null || value.isBlank()) {
            return null;
        } else {
            return value.strip();
        }
    }

    private boolean validateRequiredFields(Map<Integer, String> cellValues) {
        List<String> missingFields = new ArrayList<>();

        if (getCellValue(cellValues, BankColumn.COUNTRY_CODE) == null) {
            missingFields.add("Country Code");
        }
        if (getCellValue(cellValues, BankColumn.SWIFT_CODE) == null) {
            missingFields.add("Swift Code");
        }
        if (getCellValue(cellValues, BankColumn.CODE_TYPE) == null) {
            missingFields.add("Code Type");
        }

        return missingFields.isEmpty();
    }
}