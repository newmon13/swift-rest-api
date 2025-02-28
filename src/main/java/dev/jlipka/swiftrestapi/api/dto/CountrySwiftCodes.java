package dev.jlipka.swiftrestapi.api.dto;

import lombok.AllArgsConstructor;

import java.util.List;
@AllArgsConstructor
public class CountrySwiftCodes {
    String countryISO2;
    String countryName;
    List<SwiftCode> swiftCodes;
}