package dev.jlipka.swiftrestapi.api.validator;

import dev.jlipka.swiftrestapi.domain.model.Bank;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Locale;

import static org.springframework.validation.ValidationUtils.invokeValidator;

@Component
@Getter
public class BankValidator implements Validator {

    private final Validator swiftCodeValidator;
    private final Validator countryCodeValidator;

    public BankValidator() {
        countryCodeValidator = new CountryCodeValidator();
        swiftCodeValidator = new SwiftCodeValidator(countryCodeValidator);
    }

    public void validateCountryCode(String countryCode, Errors errors) {
        countryCodeValidator.validate(countryCode, errors);
    }

    public void validateSwiftCode(String swiftCode, Errors errors) {
        swiftCodeValidator.validate(swiftCode, errors);
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return Bank.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        if (supports(target.getClass())) {
            Bank bank = (Bank) target;
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "countryName", "country.name.empty");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "swiftCode", "swift.code.empty");
            if (errors.hasErrors()) {
                return;
            }
            invokeValidator(countryCodeValidator, bank.getCountryCode(), errors);
            invokeValidator(swiftCodeValidator, bank.getSwiftCode(), errors);
            if (!errors.hasErrors()) {
                isSameCountryCodeInSwiftCode(bank, errors);
                doesCountryCodeMatchCountryName(bank, errors);
            }
        }
    }

    private void isSameCountryCodeInSwiftCode(Bank bank, Errors errors) {
        String extractedCountryCode = bank.getSwiftCode()
                .substring(4, 6);

        if (!bank.getCountryCode()
                .equals(extractedCountryCode)) {
            errors.reject("bank.property.countryISO2.swiftCode.mismatch", "Mismatch between countryISO2 code and corresponding fragment in SWIFT code");
        }
    }

    private void doesCountryCodeMatchCountryName(Bank bank, Errors errors) {
        String expectedCountry = Locale.of("", bank.getCountryCode())
                .getDisplayCountry(Locale.ENGLISH)
                .toUpperCase();

        if (!expectedCountry.equals(bank.getCountryName())) {
            errors.reject("bank.property.countryISO2.countryName.mismatch",
                    "Mismatch between countryISO2 code and country name");
        }
    }
}
