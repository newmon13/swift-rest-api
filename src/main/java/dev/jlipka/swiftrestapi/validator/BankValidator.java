package dev.jlipka.swiftrestapi.validator;

import dev.jlipka.swiftrestapi.model.Bank;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.Locale;

import static org.springframework.validation.ValidationUtils.invokeValidator;

@Component
public class BankValidator implements Validator {
    private final Validator swiftCodeValidator;
    private final Validator countryCodeValidator;

    public BankValidator(Validator swiftCodeValidator, Validator countryCodeValidator) {
        if (swiftCodeValidator == null || countryCodeValidator == null) {
            throw new IllegalArgumentException("Supplied validators are required and must not be null.");
        }
        if (!swiftCodeValidator.supports(String.class) || !countryCodeValidator.supports(String.class)) {
            throw new IllegalArgumentException("Supplied validators must support the validation of [String] instances.");
        }
        this.swiftCodeValidator = swiftCodeValidator;
        this.countryCodeValidator = countryCodeValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Bank.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Bank bank = (Bank) target;

        ValidationUtils.rejectIfEmpty(errors, "countryCode", "country.code.empty");
        ValidationUtils.rejectIfEmpty(errors, "swiftCode", "swift.code.empty");

        if (errors.hasErrors()) {
            return;
        }

        ValidationUtils.invokeValidator(countryCodeValidator, bank.getCountryCode(), errors);
        ValidationUtils.invokeValidator(swiftCodeValidator, bank.getSwiftCode(), errors);

        if (!errors.hasErrors()) {
            isSameCountryCodeInSwiftCode(bank, errors);
        }
    }

    private void isSameCountryCodeInSwiftCode(Bank bank, Errors errors) {
        String extractedCountryCode = bank.getSwiftCode()
                .substring(4, 6);

        if (!bank.getCountryCode().equals(extractedCountryCode)) {
            errors.reject("bank.property.countryISO2.mismatch",
                    "Mismatch between countryISO2 code and corresponding fragment in SWIFT code");
        }
    }
}
