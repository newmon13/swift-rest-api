package dev.jlipka.swiftrestapi.api.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public class Headquarter extends Branch {
    final List<SwiftCode> branches;
}