package dev.jlipka.swiftrestapi.api.dto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@JsonPropertyOrder({ "address", "bankName", "countryISO2", "countryName", "isHeadquarter", "swiftCode" })
@Getter
@SuperBuilder
@NoArgsConstructor
public class Branch extends BankOffice {
    @NotBlank(message = "Country ISO2 code is required")
    String countryISO2;
    @NotBlank(message = "Country name is required")
    String countryName;
}