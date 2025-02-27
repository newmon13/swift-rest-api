package dev.jlipka.swiftrestapi.domain.logic;

import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExcelReaderTest {
    private final ExcelReader excelReader = new ExcelReader("src/test/resources");

    @ParameterizedTest
    @CsvSource({"banks.xlsx, 1", "test.xlsx, 2"})
    void shouldExtractRightNumber(String fileName, int sheets) {
        //when
        List<Sheet> extractedSheets = excelReader.getSheets(fileName);
        //then
        assertThat(extractedSheets.size()).isEqualTo(sheets);
    }

    @Test
    void shouldThrowRuntimeExceptionWhenFileNotFound() {
        // given
        String nonExistentFile = "non-existent-file.xlsx";
        // when & then
        assertThrows(RuntimeException.class, () -> excelReader.getSheets(nonExistentFile));
    }
}