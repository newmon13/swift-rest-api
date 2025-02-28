package dev.jlipka.swiftrestapi.api.dto;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "address", "bankName", "countryISO2", "isHeadquarter", "swiftCode" })
public class SwiftCode extends BankOffice {
    String countryISO2;

    public SwiftCode(String address, String bankName, boolean isHeadquarter, String swiftCode,
                    String countryISO2) {
        super(address, bankName, isHeadquarter, swiftCode);
        this.countryISO2 = countryISO2;
    }
}