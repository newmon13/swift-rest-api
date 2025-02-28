package dev.jlipka.swiftrestapi.api.validator;

import org.apache.commons.math3.geometry.enclosing.EnclosingBall;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SpreadsheetFileValidatorTest {
    SpreadsheetFileValidator validator = new SpreadsheetFileValidator();

    @ParameterizedTest
    @CsvSource({
            "test.xlsx, false",
            "test.xls, false",
            "test.pdf, true",
            "test.xl, true"
    })
    void shouldNotContainFileInvalidFormatForXlsxAndXlsFiles(String fileName, boolean isValid) {
        //given
        MultipartFile multipartFile = new MockMultipartFile(
                fileName,
                fileName,
                null,
                "test content".getBytes()
        );
        Errors errors = new BeanPropertyBindingResult(multipartFile, "file");
        //when
        validator.validate(multipartFile, errors);
        //then
        boolean foundCode = errors.getAllErrors()
                .stream()
                .flatMap(error -> Arrays.stream(Objects.requireNonNull(error.getCodes())))
                .anyMatch(code -> code.equals("file.invalid.format"));
        assertEquals(isValid, foundCode);
    }

    @ParameterizedTest
    @CsvSource({
        "6291456, true",   // 6MB
        "5242880, false",  // 5MB
        "4194304, false"   // 4MB
    })
    void shouldContainFileTooLargeErrorWhenPassedFileIsBiggerThan5MB(long fileSize, boolean shouldHaveError) {
        //given
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getSize()).thenReturn(fileSize);
        when(multipartFile.getOriginalFilename()).thenReturn("testFileName.xlsx");
        when(multipartFile.getName()).thenReturn("file");
        when(multipartFile.isEmpty()).thenReturn(false);
        Errors errors = new BeanPropertyBindingResult(multipartFile, "file");
        //when
        validator.validate(multipartFile, errors);
        //then
        if (shouldHaveError) {
            assertThat(errors.getAllErrors()).hasSize(1);
            boolean foundCode = errors.getAllErrors()
                    .stream()
                    .flatMap(error -> Arrays.stream(Objects.requireNonNull(error.getCodes())))
                    .anyMatch(code -> code.equals("file.too.large"));
            assertTrue(foundCode);
        } else {
            assertThat(errors.getAllErrors()).isEmpty();
        }
    }
}