package dev.jlipka.swiftrestapi.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankOffice {
    String address;
    String bankName;
    @JsonProperty("isHeadquarter")
    boolean isHeadquarter;
    @NotBlank(message = "SWIFT code is required")
    String swiftCode;
}