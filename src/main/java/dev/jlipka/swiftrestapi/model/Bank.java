package dev.jlipka.swiftrestapi.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.TimeZone;

@Document
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bank {
    @Id
    private String id;
    private String countryCode;
    private String swiftCode;
    private String codeType;
    private String name;
    private String address;
    private String townName;
    private String countryName;
    private String timeZone;
}
