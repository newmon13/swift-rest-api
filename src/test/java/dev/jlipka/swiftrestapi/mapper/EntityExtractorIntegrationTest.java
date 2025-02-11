package dev.jlipka.swiftrestapi.mapper;

import dev.jlipka.swiftrestapi.model.Bank;
import dev.jlipka.swiftrestapi.validator.XlsxValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityExtractorIntegrationTest {

    private BankMapper bankMapper;
    private XlsxValidator xlsxValidator;
    private SpreadsheetReader spreadsheetReader;
    private EntityExtractor<Bank> entityExtractor;


    @BeforeEach
    void setUp() {
        bankMapper = new BankMapper();
        xlsxValidator = new XlsxValidator();
        spreadsheetReader = new SpreadsheetReader("test.xlsx");
        entityExtractor = new EntityExtractor<>(xlsxValidator, bankMapper, spreadsheetReader);
    }

    @Test
    void shouldReturnProperlyMappedEntitiesFromMultipleSheets_IntegrationTest() {
        //when
        List<Bank> extract = entityExtractor.extract(true);
        //then
        assertThat(extract.size()).isEqualTo(4);
        assertThat(extract).isNotNull();
        assertThat(extract).isNotEmpty();
        assertThat(extract).doesNotContainNull();
    }
}
