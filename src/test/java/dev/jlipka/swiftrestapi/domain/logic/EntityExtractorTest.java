package dev.jlipka.swiftrestapi.domain.logic;

import dev.jlipka.swiftrestapi.api.mapper.entity.BankMapper;
import dev.jlipka.swiftrestapi.domain.model.Bank;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class EntityExtractorTest {
    @Mock
    BankMapper bankMapper;
    EntityExtractor<Bank> entityExtractor;

    @BeforeEach
    void setUp() {
        lenient().when(bankMapper.from(any(Iterator.class)))
                .thenReturn(new Bank());
        entityExtractor = new EntityExtractor<>(bankMapper);
    }

    private Sheet getExampleSheetWithHeaderRow() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("example");

        Row headerRow = sheet.createRow(0);
        Cell headerCell1 = headerRow.createCell(0);
        headerCell1.setCellValue("countryISO2code");
        Cell headerCell2 = headerRow.createCell(1);
        headerCell2.setCellValue("swiftCode");
        Cell headerCell3 = headerRow.createCell(2);
        headerCell3.setCellValue("codeType");

        Row dataRow1 = sheet.createRow(1);
        dataRow1.createCell(0)
                .setCellValue("AL");
        dataRow1.createCell(1)
                .setCellValue("AAISALTRXXX");
        dataRow1.createCell(2)
                .setCellValue("BIC11");
        return sheet;
    }


    private Sheet getExampleSheetWithBlankRows() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("example");

        for (int i = 0; i < 3; i++) {
            sheet.createRow(i);
        }
        return sheet;
    }

    @Test
    void shouldSkipHeaderRowAndExtractOneEntity() {
        //given
        Sheet sheet = getExampleSheetWithHeaderRow();
        //when
        List<Bank> extract = entityExtractor.extract(sheet, true);
        //then
        assertThat(extract).hasSize(1);
        assertThat(sheet.getPhysicalNumberOfRows()).isEqualTo(2);
    }

    @Test
    void shouldNotSkipHeaderRowAndExtractTwoEntities() {
        //given
        Sheet sheet = getExampleSheetWithHeaderRow();
        //when
        List<Bank> extract = entityExtractor.extract(sheet, false);
        //then
        assertThat(extract).hasSize(2);
        assertThat(sheet.getPhysicalNumberOfRows()).isEqualTo(2);
    }

    @Test
    void shouldExtractNoEntitiesWhenThereAreOnlyBlankRowsInFile() {
        //given
        Sheet sheet = getExampleSheetWithBlankRows();
        //when
        List<Bank> extract = entityExtractor.extract(sheet, false);
        //then
        assertThat(extract).hasSize(0);
    }
}