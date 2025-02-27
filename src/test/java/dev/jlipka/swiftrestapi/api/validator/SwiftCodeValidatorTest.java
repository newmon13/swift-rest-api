package dev.jlipka.swiftrestapi.api.validator;

import dev.jlipka.swiftrestapi.domain.model.Bank;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SwiftCodeValidatorTest {
    @Mock
    CountryCodeValidator countryCodeValidator;
    SwiftCodeValidator swiftCodeValidator;

    @BeforeEach
    void setUp() {
        when(countryCodeValidator.supports(String.class)).thenReturn(true);
        swiftCodeValidator = new SwiftCodeValidator(countryCodeValidator);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenInitializedValidatorWithNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            swiftCodeValidator = new SwiftCodeValidator(null);
        });
    }

    @Test
    void shouldReturnTrueWhenPassedString() {
        //given
        Class<?> clazz = String.class;
        //when
        boolean supports = swiftCodeValidator.supports(clazz);
        //then
        assertThat(supports).isTrue();
    }

    @Test
    void shouldReturnFalseWhenPassedSthElseThanString() {
        //given
        Class<?> clazz = Bank.class;
        //when
        boolean supports = swiftCodeValidator.supports(clazz);
        //then
        assertThat(supports).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"ABCDEFGHIJKL", "ABCDEFGHIJ"})
    void shouldContainSwiftCodeLengthInvalidErrorWhenLengthIsDifferentThan11characters(String swiftCode) {
        //given
        Errors errors = new BeanPropertyBindingResult(swiftCode, "swiftCode");
        //when
        swiftCodeValidator.validate(swiftCode, errors);
        //then
        boolean foundCode = errors.getAllErrors()
                .stream()
                .flatMap(error -> Arrays.stream(Objects.requireNonNull(error.getCodes())))
                .anyMatch(code -> code.equals("swiftCode.length.invalid"));
        assertThat(foundCode).isTrue();
    }

    @Test
    void shouldNotContainSwiftCodeLengthInvalidErrorWhenLengthIs11() {
        //given
        String swiftCode = "ABCDEFGHIJK";
        Errors errors = new BeanPropertyBindingResult(swiftCode, "swiftCode");
        //when
        swiftCodeValidator.validate(swiftCode, errors);
        //then
        boolean foundCode = errors.getAllErrors()
                .stream()
                .flatMap(error -> Arrays.stream(Objects.requireNonNull(error.getCodes())))
                .anyMatch(code -> code.equals("swiftCode.length.invalid"));
        assertThat(foundCode).isFalse();
    }

    @ParameterizedTest
    @CsvSource({
            "AAISALTRXXX, false",
            "9999ALTRXXX, true",
            "@$%.ALTRXXX, true",
            "aaisaltrxxx, true"
    })
    void shouldContainSwiftCodeBankCodeInvalidErrorWhenBankCodeIsNotFourUpperCaseLettersLongSubstring(String swiftCode, boolean isValid) {
        //given
        Errors errors = new BeanPropertyBindingResult(swiftCode, "swiftCode");
        //when
        swiftCodeValidator.validate(swiftCode, errors);
        //then
        boolean foundCode = errors.getAllErrors()
                .stream()
                .flatMap(error -> Arrays.stream(Objects.requireNonNull(error.getCodes())))
                .anyMatch(code -> code.equals("swiftCode.bank.code.invalid"));
        assertEquals(isValid, foundCode);
    }

    @ParameterizedTest
    @CsvSource({
            "AAISALTRXXX, false",
            "AAISAL00XXX, false",
            "AAISAL3$XXX, true",
            "AAISAL  XXX, true"
    })
    void shouldContainSwiftCodeLocationCodeInvalidErrorWhenLocationCodeIsNotTwoAlphanumericCharactersLongSubstring(String swiftCode, boolean isValid) {
        //given
        Errors errors = new BeanPropertyBindingResult(swiftCode, "swiftCode");
        //when
        swiftCodeValidator.validate(swiftCode, errors);
        //then
        boolean foundCode = errors.getAllErrors()
                .stream()
                .flatMap(error -> Arrays.stream(Objects.requireNonNull(error.getCodes())))
                .anyMatch(code -> code.equals("swiftCode.location.code.invalid"));
        assertEquals(isValid, foundCode);
    }

    @ParameterizedTest
    @CsvSource({
            "AAISALTRXXX, false",
            "AAISALTR..., true",
            "AAISALTRXX, false", // different error code will be put in Errors (length constraint violated)
            "AAISALTRXXXX, false", // same here
            "AAISALTR333, false",
            "AAISALTR@42, true",
    })
    public void shouldContainSwiftCodeBranchCodeInvalidErrorWhenBranchCodeIsNotThreeAlphanumericCharactersLongSubstring(String swiftCode, boolean isValid) {
        //given
        Errors errors = new BeanPropertyBindingResult(swiftCode, "swiftCode");
        //when
        swiftCodeValidator.validate(swiftCode, errors);
        //then
        boolean foundCode = errors.getAllErrors()
                .stream()
                .flatMap(error -> Arrays.stream(Objects.requireNonNull(error.getCodes())))
                .anyMatch(code -> code.equals("swiftCode.branch.code.invalid"));
        assertEquals(isValid, foundCode);
    }


    @Test
    void shouldContainAllPossibleErrorsExceptCountryCodeOne() {
        //given
        String swiftCode= ",,,,,,,,,,,";
        Errors errors = new BeanPropertyBindingResult(swiftCode, "swiftCode");
        //when
        swiftCodeValidator.validate(swiftCode, errors);
        //then
        assertThat(errors.getAllErrors()).hasSize(3);
    }
}