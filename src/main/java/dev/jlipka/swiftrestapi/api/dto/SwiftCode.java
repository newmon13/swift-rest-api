package dev.jlipka.swiftrestapi.api.dto;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonPropertyOrder({ "address", "bankName", "countryISO2", "isHeadquarter", "swiftCode" })
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SwiftCode extends BankOffice {
    @NotBlank(message = "Country ISO2 code is required")
    String countryISO2;

    public SwiftCode(String address, String bankName, boolean isHeadquarter, String swiftCode,
                     String countryISO2) {
        super(address, bankName, isHeadquarter, swiftCode);
        this.countryISO2 = countryISO2;
    }
}