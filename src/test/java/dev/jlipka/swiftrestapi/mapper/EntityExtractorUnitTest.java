package dev.jlipka.swiftrestapi.mapper;

import dev.jlipka.swiftrestapi.model.Bank;
import dev.jlipka.swiftrestapi.validator.ValidationResult;
import dev.jlipka.swiftrestapi.validator.XlsxValidator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntityExtractorUnitTest {

    @Mock
    private BankMapper bankMapper;
    @Mock
    XlsxValidator xlsxValidator;
    @Mock
    ClassPathResourceSpreadsheetReader classPathResourceSpreadsheetReader;
    @InjectMocks
    private EntityExtractor<Bank> entityExtractor;

    private Workbook workbook;

    @BeforeEach
    void setUp() {
        workbook = new HSSFWorkbook();
    }

    @Test
    public void shouldExtractBankFromSingleSheetWithSingleRowWithoutHeaders() {
        // given
        Sheet sheet = workbook.createSheet();

        Row row = sheet.createRow(1);
        Cell cell = row.createCell(0);
        cell.setCellValue("testValue");

        ValidationResult validationSuccess = new ValidationResult(true, "");

        doReturn(List.of(sheet)).when(classPathResourceSpreadsheetReader).getSheets();
        doReturn(validationSuccess).when(xlsxValidator).validate(any());
        when(bankMapper.mapRowToEntity(any()))
                .thenReturn(Optional.of(Bank.builder().countryCode("testValue").build()));
        // when
        List<Bank> extractedBanks = entityExtractor.extract(false);
        // then
        assertThat(extractedBanks).hasSize(1);
        assertThat(extractedBanks)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("countryCode", "testValue");
        verify(bankMapper, times(1)).mapRowToEntity(any());
    }

    @Test
    public void shouldNotExtractAnyBankFromEmptyFile() {
        // given
        Sheet sheet = workbook.createSheet();
        ValidationResult validationSuccess = new ValidationResult(true, "");

        doReturn(List.of(sheet)).when(classPathResourceSpreadsheetReader).getSheets();
        doReturn(validationSuccess).when(xlsxValidator).validate(any());
        // when
        List<Bank> extractedBanks = entityExtractor.extract(false);
        // then
        assertThat(extractedBanks).isEmpty();
    }

    @Test
    void shouldNotExtractBankFromSingleSheetWithSingleRowWithHeaders() {
        // given
        Sheet sheet = workbook.createSheet();

        Row row = sheet.createRow(1);
        Cell cell = row.createCell(0);
        cell.setCellValue("testValue");

        ValidationResult validationSuccess = new ValidationResult(true, "");

        doReturn(List.of(sheet)).when(classPathResourceSpreadsheetReader).getSheets();
        doReturn(validationSuccess).when(xlsxValidator).validate(any());
        // when
        List<Bank> extractedBanks = entityExtractor.extract(true);
        // then
        assertThat(extractedBanks).hasSize(0);
        verify(bankMapper, times(0)).mapRowToEntity(any());
    }
}