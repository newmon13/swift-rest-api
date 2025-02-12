package dev.jlipka.swiftrestapi.mapper;

import dev.jlipka.swiftrestapi.model.Bank;
import dev.jlipka.swiftrestapi.validator.XlsxValidator;
import org.apache.commons.math3.analysis.function.Add;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityExtractorIntegrationTest {
    private BankMapper bankMapper;
    private EntityExtractor<Bank> entityExtractor;

    @BeforeEach
    void setUp() throws IOException {
        bankMapper = new BankMapper();
        ClassPathResource classPathResource = new ClassPathResource("test.xlsx");
        Workbook workbook = WorkbookFactory.create(classPathResource.getFile());
        List<Sheet> sheets = new ArrayList<>();
        workbook.sheetIterator().forEachRemaining(sheets::add);
        entityExtractor = new EntityExtractor<>(sheets, bankMapper);
    }

    @Test
    void shouldReturnProperlyMappedEntitiesFromMultipleSheets() {
        //when
        List<Bank> extract = entityExtractor.extract(true);
        //then
        assertThat(extract.size()).isEqualTo(4);
        assertThat(extract).doesNotContainNull();
        //first sheet
        assertThat(extract.get(0)).hasFieldOrPropertyWithValue("countryCode", "AL")
                .hasFieldOrPropertyWithValue("timeZone", "Europe/Tirane");
        assertThat(extract.get(1)).hasFieldOrPropertyWithValue("countryCode", "BG")
                .hasFieldOrPropertyWithValue("timeZone", "Europe/Sofia");
        //second sheet
        assertThat(extract.get(2)).hasFieldOrPropertyWithValue("countryCode", "BG")
                .hasFieldOrPropertyWithValue("timeZone", "Europe/Sofia");
        assertThat(extract.get(3)).hasFieldOrPropertyWithValue("countryCode", "UY")
                .hasFieldOrPropertyWithValue("timeZone", "America/Montevideo");

    }
}
