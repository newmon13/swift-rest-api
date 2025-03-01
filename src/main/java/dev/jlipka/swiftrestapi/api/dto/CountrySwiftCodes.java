package dev.jlipka.swiftrestapi.api.dto;

import java.util.List;

public record CountrySwiftCodes(String countryISO2, String countryName, List<SwiftCode> swiftCodes) {

}