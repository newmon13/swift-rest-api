package dev.jlipka.swiftrestapi.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TypeAlias("bank")
public class Bank {

    @Id
    private String swiftCode;
    private String countryCode;
    private String codeType;
    private String name;
    private String address;
    private String townName;
    private String countryName;
    private String timeZone;
    @DBRef(lazy = true)
    @ToString.Exclude
    @Field("headquarter")
    private Bank headquarter;
}
