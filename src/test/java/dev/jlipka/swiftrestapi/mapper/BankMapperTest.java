package dev.jlipka.swiftrestapi.mapper;

import dev.jlipka.swiftrestapi.model.Bank;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BankMapperTest {

    private BankMapper bankMapper = new BankMapper();

    @Test
    public void shouldMapRowWithAllRequiredCellsToBank() {
        //given
        Cell cell1 = mock(Cell.class);
        Cell cell2 = mock(Cell.class);
        Cell cell3 = mock(Cell.class);
        when(cell1.getStringCellValue()).thenReturn("AL");
        when(cell1.getColumnIndex()).thenReturn(0);

        when(cell2.getStringCellValue()).thenReturn("AAISALTRXXX");
        when(cell2.getColumnIndex()).thenReturn(1);

        when(cell3.getStringCellValue()).thenReturn("BIC11");
        when(cell3.getColumnIndex()).thenReturn(2);

        List<Cell> cells = List.of(cell1, cell2, cell3);
        Iterator<Cell> iterator = cells.iterator();
        //when
        Bank bank = bankMapper.mapRowToEntity(iterator).get();
        //then
        assertThat(bank.getCountryCode()).isEqualTo("AL");
        assertThat(bank.getSwiftCode()).isEqualTo("AAISALTRXXX");
        assertThat(bank.getCodeType()).isEqualTo("BIC11");
    }

    @Test
    public void shouldNotMapRowWithMissingRequiredCellsToBank() {
        //given
        Cell cell1 = mock(Cell.class);
        when(cell1.getStringCellValue()).thenReturn("AL");
        when(cell1.getColumnIndex()).thenReturn(0);
        //when
        List<Cell> cells = List.of(cell1);
        Iterator<Cell> iterator = cells.iterator();
        Optional<Bank> bank = bankMapper.mapRowToEntity(iterator);
        //then
        assertThat(bank).isNotPresent();
    }

    @Test
    public void shouldMapRealRowCellsToBank() throws IOException {
        //given
        ClassPathResource classPathResource = new ClassPathResource("banks.xlsx");
        Workbook workbook = new XSSFWorkbook(classPathResource.getInputStream());
        Sheet sheetAt = workbook.getSheetAt(0);
        Iterator<Row> iterator = sheetAt.iterator();
        iterator.next();
        Row row = iterator.next();
        Iterator<Cell> cellIterator = row.cellIterator();
        //when
        Bank bank = bankMapper.mapRowToEntity(cellIterator).get();
        //then
        assertThat(bank.getCountryCode()).isEqualTo("AL");
        assertThat(bank.getSwiftCode()).isEqualTo("AAISALTRXXX");
        assertThat(bank.getCodeType()).isEqualTo("BIC11");
        assertThat(bank.getName()).isEqualTo("UNITED BANK OF ALBANIA SH.A");
        assertThat(bank.getAddress()).isEqualTo("HYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023");
        assertThat(bank.getTownName()).isEqualTo("TIRANA");
        assertThat(bank.getCountryName()).isEqualTo("ALBANIA");
        assertThat(bank.getTimeZone()).isEqualTo("Europe/Tirane");
    }

    @Test
    void shouldSetBlankCellAsNull() throws IOException {
        //given
        ClassPathResource classPathResource = new ClassPathResource("banks.xlsx");
        Workbook workbook = new XSSFWorkbook(classPathResource.getInputStream());
        Sheet sheetAt = workbook.getSheetAt(0);
        Row row = sheetAt.getRow(23);
        //when
        Bank bank = bankMapper.mapRowToEntity(row.cellIterator()).get();
        //then
        assertThat(bank.getAddress()).isNull();
    }
}