package dev.jlipka.swiftrestapi.api.validator;

import dev.jlipka.swiftrestapi.domain.model.Bank;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BankValidatorTest {
    BankValidator bankValidator = new BankValidator();

    @Test
    void shouldReturnTrueWhenPassedObjectOfBankInstance() {
        // given
        Class<Bank> bankClass = Bank.class;
        // when
        boolean supports = bankValidator.supports(bankClass);
        // then
        assertThat(supports).isTrue();
    }

    @Test
    void shouldReturnFalseWhenPassedObjectOfNotBankClass() {
        //given
        Class<Object> bankClass = Object.class;
        // when
        boolean supports = bankValidator.supports(bankClass);
        // then
        assertThat(supports).isFalse();
    }

    @Test
    void shouldContainCountryNameEmptyErrorWhenPassedBankObjectWithNullAsCountryCode() {
        //given
        Bank bank = new Bank("testSwiftCode", "testCountryCode", "testCodeType", "testBankName", "testAddress", "testTownName", null, "testCountryZone", null);
        Errors errors = new BeanPropertyBindingResult(bank, "bank");
        //when
        bankValidator.validate(bank, errors);
        //then
        assertThat(errors.getAllErrors()).hasSize(1);
        assertThat(errors.getFieldError("countryName")).isNotNull()
                .extracting("code")
                .isEqualTo("country.name.empty");
    }


    @Test
    void shouldContainCountryNameEmptyErrorWhenPassedBankObjectWithEmptyStringAsCountryCode() {
        //given
        Bank bank = new Bank("testSwiftCode", "testCountryCode", "testCodeType", "testBankName", "testAddress", "testTownName", "", "testCountryZone", null);
        Errors errors = new BeanPropertyBindingResult(bank, "bank");
        //when
        bankValidator.validate(bank, errors);
        //then
        assertThat(errors.getAllErrors()).hasSize(1);
        assertThat(errors.getFieldError("countryName")).isNotNull()
                .extracting("code")
                .isEqualTo("country.name.empty");
    }

    @Test
    void shouldContainSwiftCodeEmptyErrorWhenPassedBankObjectWithNullAsSwiftCode() {
        //given
        Bank bank = new Bank(null, "testCountryCode", "testCodeType", "testBankName", "testAddress", "testTownName", "testCountryName", "testCountryZone", null);
        Errors errors = new BeanPropertyBindingResult(bank, "bank");
        //when
        bankValidator.validate(bank, errors);
        //then
        assertThat(errors.getAllErrors()).hasSize(1);
        assertThat(errors.getFieldError("swiftCode")).isNotNull()
                .extracting("code")
                .isEqualTo("swift.code.empty");
    }

    @Test
    void shouldContainSwiftCodeEmptyErrorWhenPassedBankObjectWithEmptyStringAsSwiftCode() {
        //given
        Bank bank = new Bank("", "testCountryCode", "testCodeType", "testBankName", "testAddress", "testTownName", "testCountryName", "testCountryZone", null);
        Errors errors = new BeanPropertyBindingResult(bank, "bank");
        //when
        bankValidator.validate(bank, errors);
        //then
        assertThat(errors.getAllErrors()).hasSize(1);
        assertThat(errors.getFieldError("swiftCode")).isNotNull()
                .extracting("code")
                .isEqualTo("swift.code.empty");
    }

    @Test
    void shouldContainBankPropertyCountryNameMismatchError() {
        //given
        Bank bank = new Bank("AAAAPLAAAAA",
                "PL",
                "testCodeType",
                "testBankName",
                "testAddress",
                "testTownName",
                "GERMANY",
                "testCountryZone",
                null);
        Errors errors = new BeanPropertyBindingResult(bank, "bank");
        //when
        bankValidator.validate(bank, errors);
        //then
        boolean foundCode = errors.getAllErrors()
                .stream()
                .flatMap(error -> Arrays.stream(Objects.requireNonNull(error.getCodes())))
                .anyMatch(code -> code.equals("bank.property.countryISO2.countryName.mismatch"));



        errors.getAllErrors().stream().flatMap(objectError ->
                Arrays.stream(Objects.requireNonNull(objectError.getCodes()))).forEach(System.out::println);

        assertThat(foundCode).isTrue();
    }


    @Test
    void shouldContainBankPropertyCountryISO2MismatchError() {
        //given
        Bank bank = new Bank("AAAAFRAAAAA",
                "PL",
                "testCodeType",
                "testBankName",
                "testAddress",
                "testTownName",
                "POLAND",
                "testCountryZone",
                null);
        Errors errors = new BeanPropertyBindingResult(bank, "bank");
        //when
        bankValidator.validate(bank, errors);
        //then
        boolean foundCode = errors.getAllErrors()
                .stream()
                .flatMap(error -> Arrays.stream(Objects.requireNonNull(error.getCodes())))
                .anyMatch(code -> code.equals("bank.property.countryISO2.swiftCode.mismatch"));
        assertThat(foundCode).isTrue();
    }


    @Test
    void shouldContainBothCountryISO2AndCountryNameMismatchErrors() {
        //given
        Bank bank = new Bank("AAAAFRAAAAA", "PL", "testCodeType", "testBankName", "testAddress", "testTownName", "GERMANY", "testCountryZone", null);
        Errors errors = new BeanPropertyBindingResult(bank, "bank");
        //when
        bankValidator.validate(bank, errors);
        //then
        assertThat(errors.getAllErrors()).hasSize(2);
        boolean countryISO2mismatchError = errors.getAllErrors()
                .stream()
                .flatMap(error -> Arrays.stream(Objects.requireNonNull(error.getCodes())))
                .anyMatch(code -> code.equals("bank.property.countryISO2.swiftCode.mismatch"));

        boolean countryNameMismatchError = errors.getAllErrors()
                .stream()
                .flatMap(error -> Arrays.stream(Objects.requireNonNull(error.getCodes())))
                .anyMatch(code -> code.equals("bank.property.countryISO2.countryName.mismatch"));
        assertThat(countryISO2mismatchError).isTrue();
        assertThat(countryNameMismatchError).isTrue();
    }
}