package dev.jlipka.swiftrestapi.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class XlsxValidatorTest {

    XlsxValidator validator;

    @BeforeEach
    void setUp() {
        validator = new XlsxValidator();
    }

    @Test
    void shouldReturnValidationResultTrueAndCorrespondingMessageWhenFileIsXlsx() {
        //given
        File testFile = mock(File.class);
        when(testFile.getName()).thenReturn("test.xlsx");

        //when
        ValidationResult validate = validator.validate(testFile);

        //then
        assertThat(validate.result()).isTrue();
        assertThat(validate.message()).contains("File validated successfully");
    }

    @Test
    void shouldReturnValidationResultFalseAndCorrespondingMessageWhenFileIsNotXlsx() {
        //given
        File testFile = mock(File.class);
        when(testFile.getName()).thenReturn("test.txt");

        //when
        ValidationResult validate = validator.validate(testFile);

        //then
        assertThat(validate.result()).isFalse();
        assertThat(validate.message()).doesNotContain("File validated successfully");
        assertThat(validate.message()).contains("File is not .xlsx");
    }

    @Test
    void shouldReturnValidationResultFalseAndCorrespondingMessageWhenFileIsEmpty() {

    }
}