package dev.jlipka.swiftrestapi.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bank {
    @Id
    private String id;
    @Indexed(unique = true)
    private String swiftCode;
    private String countryCode;
    private String codeType;
    private String name;
    private String address;
    private String townName;
    private String countryName;
    private String timeZone;
    @DBRef
    private Bank headquarter;
}
