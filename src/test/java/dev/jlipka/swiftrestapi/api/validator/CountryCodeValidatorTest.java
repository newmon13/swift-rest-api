package dev.jlipka.swiftrestapi.api.validator;

import dev.jlipka.swiftrestapi.domain.model.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CountryCodeValidatorTest {
    CountryCodeValidator validator = new CountryCodeValidator();

    @Test
    void shouldReturnTrueWhenPassedString() {
        //given
        Class<?> clazz = String.class;
        //when
        boolean supports = validator.supports(clazz);
        //then
        assertThat(supports).isTrue();
    }

    @Test
    void shouldReturnFalseWhenPassedSthElseThanString() {
        //given
        Class<?> clazz = Bank.class;
        //when
        boolean supports = validator.supports(clazz);
        //then
        assertThat(supports).isFalse();
    }

    @Test
    void shouldContainCountryCodeFormatErrorWhenCountryCodeIsNotTwoCharactersLong() {
        //given
        String countryCode = "failingCountryCode";
        Errors errors = new BeanPropertyBindingResult(countryCode, "countryCode");
        //when
        validator.validate(countryCode, errors);
        //then
        assertThat(errors.getAllErrors()).hasSize(1);
        boolean foundCode = errors.getAllErrors()
                .stream()
                .flatMap(error -> Arrays.stream(Objects.requireNonNull(error.getCodes())))
                .anyMatch(code -> code.equals("country.code.format"));
        assertThat(foundCode).isTrue();
    }

    @Test
    void shouldNotContainCountryCodeFormatErrorWhenCountryCodeIsTwoCharactersLong() {
        //given
        String countryCode = "PL";
        Errors errors = new BeanPropertyBindingResult(countryCode, "countryCode");
        //when
        validator.validate(countryCode, errors);
        //then
        assertThat(errors.getAllErrors()).hasSize(0);
        boolean foundCode = errors.getAllErrors()
                .stream()
                .flatMap(error -> Arrays.stream(Objects.requireNonNull(error.getCodes())))
                .anyMatch(code -> code.equals("country.code.format"));
        assertThat(foundCode).isFalse();
    }

    @Test
    void shouldContainCountryCodeNotFoundErrorWhenCountryISO2CodeIsNotValidOrNotKnown() {
        //given
        String countryCode = "QQ";
        Errors errors = new BeanPropertyBindingResult(countryCode, "countryCode");
        //when
        validator.validate(countryCode, errors);
        //then
        assertThat(errors.getAllErrors()).hasSize(1);
        boolean foundCode = errors.getAllErrors()
                .stream()
                .flatMap(error -> Arrays.stream(Objects.requireNonNull(error.getCodes())))
                .anyMatch(code -> code.equals("country.code.not.found"));
        assertThat(foundCode).isTrue();
    }
}