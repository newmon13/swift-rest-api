package dev.jlipka.swiftrestapi.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BankOffice {
    String address;
    String bankName;
    boolean isHeadquarter;
    @NotBlank(message = "SWIFT code is required")
    String swiftCode;
}