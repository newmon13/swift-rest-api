package dev.jlipka.swiftrestapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@AllArgsConstructor
public class BankOffice {
    String address;
    String bankName;
    boolean isHeadquarter;
    String swiftCode;
}