package dev.jlipka.swiftrestapi.api.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static java.util.Locale.getISOCountries;

public class CountryCodeValidator implements Validator {

    private static final List<String> ISO_COUNTRIES = asList(getISOCountries());
    private static final Pattern COUNTRY_CODE_PATTERN = Pattern.compile("^[A-Z]{2}$");

    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String countryCode = (String) target;

        if (!isValid(countryCode)) {
            errors.reject("country.code.format", "Format of country code is not valid");
            return;
        }

        if (!exists(countryCode)) {
            errors.reject("country.code.not.found", "No country with given country code was found. Check country code and relevant part of SWIFT code");
        }
    }

    private boolean isValid(String countryCode) {
        return countryCode.matches(COUNTRY_CODE_PATTERN.pattern());
    }

    private boolean exists(String countryCode) {
        return ISO_COUNTRIES.contains(countryCode);
    }
}