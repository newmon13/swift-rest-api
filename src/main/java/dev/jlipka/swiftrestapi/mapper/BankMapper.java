package dev.jlipka.swiftrestapi.mapper;

import dev.jlipka.swiftrestapi.model.Bank;
import org.apache.poi.ss.usermodel.Cell;

import java.util.Iterator;

public class BankMapper implements RowMapper<Bank> {
    @Override
    public Bank mapRowToEntity(Iterator<Cell> rowCells) {
        Bank bank = new Bank();
        int columnIndex = 0;

        while (rowCells.hasNext()) {
            Cell cell = rowCells.next();
            setCellValueToBank(bank, columnIndex, cell);
            columnIndex++;
        }
        return bank;
    }

    private void setCellValueToBank(Bank bank, int columnIndex, Cell cell) {
        String cellValue = cellValueExists(cell.getStringCellValue());
        switch (columnIndex) {
            case 0:
                bank.setCountryCode(cellValue);
                break;
            case 1:
                bank.setSwiftCode(cellValue);
                break;
            case 2:
                bank.setCodeType(cellValue);
                break;
            case 3:
                bank.setName(cellValue);
                break;
            case 4:
                bank.setAddress(cellValue);
                break;
            case 5:
                bank.setTownName(cellValue);
                break;
            case 6:
                bank.setCountryName(cellValue);
                break;
            case 7:
                bank.setTimeZone(cellValue);
                break;
        }
    }

    private String cellValueExists(String cellValue) {
        if(cellValue.strip().isBlank()) {
            return null;
        } else {
            return cellValue.strip();
        }
    }
}
