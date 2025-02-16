package dev.jlipka.swiftrestapi.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static org.springframework.validation.ValidationUtils.invokeValidator;

@Component
public class SwiftCodeValidator implements Validator {
    private final Validator countryCodeValidator;

    public SwiftCodeValidator(Validator countryCodeValidator) {
        if (countryCodeValidator == null) {
            throw new IllegalArgumentException("Supplied validator [CountryCodeValidator] is required and must not be null.");
        }
        if (!countryCodeValidator.supports(String.class)) {
            throw new IllegalArgumentException("Supplied validator [CountryCodeValidator] must support the validation of [String] instances.");
        }

        this.countryCodeValidator = countryCodeValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String swiftCode = (String) target;
        String bankCode = swiftCode.substring(0,4);
        String countryCode = swiftCode.substring(4,6);
        String locationCode = swiftCode.substring(6,8);
        String branchCode = swiftCode.substring(8);

        isLengthCorrect(swiftCode, errors);
        validateBankCode(bankCode, errors);
        invokeValidator(countryCodeValidator, countryCode, errors);

        validateLocationCode(locationCode, errors);
        validateBranchCode(branchCode, errors);
    }

    private void isLengthCorrect(String swiftCode, Errors errors) {
        if (swiftCode.length() != 11) {
            errors.reject("swift-code.length.invalid");
        }
    }

    private void validateBankCode(String bankCode, Errors errors) {
        if (!bankCode.matches("^[A-Z]{4}$")) {
            errors.reject("swift-code.bank-code.invalid", "Bank code must contain 4 letters");
        }
    }

    private void validateLocationCode(String locationCode, Errors errors) {
        if (!locationCode.matches("^[A-Z0-9]{2}$")) {
            errors.reject("swift-code.location-code.invalid",
                    "Location code must contain 2 alphanumeric characters");
        }
    }

    private void validateBranchCode(String branchCode, Errors errors) {
        if (!branchCode.matches("^(XXX|[A-Z0-9]{3})$")) {
            errors.reject("swift-code.branch-code.invalid",
                    "Branch code must be 'XXX' for head office or 3 alphanumeric characters");
        }
    }
}
