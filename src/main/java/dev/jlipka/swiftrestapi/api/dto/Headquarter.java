package dev.jlipka.swiftrestapi.api.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@JsonPropertyOrder({ "address", "bankName", "countryISO2", "countryName", "isHeadquarter", "swiftCode", "branches"})
@SuperBuilder
@Getter
public class Headquarter extends Branch {
    final List<SwiftCode> branches;
}