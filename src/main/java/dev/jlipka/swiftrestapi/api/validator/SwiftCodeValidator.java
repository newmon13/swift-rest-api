package dev.jlipka.swiftrestapi.api.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static org.springframework.validation.ValidationUtils.invokeValidator;

public class SwiftCodeValidator implements Validator {

    private final Validator countryCodeValidator;

    public SwiftCodeValidator(Validator countryCodeValidator) {
        this.countryCodeValidator = countryCodeValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String swiftCode = (String) target;
        String bankCode = swiftCode.substring(0, 4);
        isLengthCorrect(swiftCode, errors);
        if (errors.hasErrors()) return;

        String countryCode = swiftCode.substring(4, 6);
        String locationCode = swiftCode.substring(6, 8);
        String branchCode = swiftCode.substring(8);

        invokeValidator(countryCodeValidator, countryCode, errors);
        isBankCodeValid(bankCode, errors);
        isLocationCodeValid(locationCode, errors);
        isBranchCodeValid(branchCode, errors);
    }

    private void isLengthCorrect(String swiftCode, Errors errors) {
        if (swiftCode.length() != 11) {
            errors.reject("swiftCode.length.invalid", "SWIFT code must be 11 characters long");
        }
    }

    private void isBankCodeValid(String bankCode, Errors errors) {
        if (!bankCode.matches("^[A-Z]{4}$")) {
            errors.reject("swiftCode.bank.code.invalid", "Bank code must contain 4 letters");
        }
    }

    private void isLocationCodeValid(String locationCode, Errors errors) {
        if (!locationCode.matches("^[A-Z0-9]{2}$")) {
            errors.reject("swiftCode.location.code.invalid", "Location code must contain 2 alphanumeric characters");
        }
    }

    private void isBranchCodeValid(String branchCode, Errors errors) {
        if (!branchCode.matches("^[A-Z0-9]{3}$")) {
            errors.reject("swiftCode.branch.code.invalid", "Branch code must be 'XXX' for head office or 3 alphanumeric characters");
        }
    }
}
