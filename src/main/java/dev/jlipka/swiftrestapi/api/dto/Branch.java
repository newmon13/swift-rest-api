package dev.jlipka.swiftrestapi.api.dto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;


@JsonPropertyOrder({ "address", "bankName", "countryISO2", "countryName", "isHeadquarter", "swiftCode" })
@Getter
@SuperBuilder
public class Branch extends BankOffice {
    String countryISO2;
    String countryName;
}