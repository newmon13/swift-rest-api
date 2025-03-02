package dev.jlipka.swiftrestapi.service;

import dev.jlipka.swiftrestapi.api.dto.CountrySwiftCodes;
import dev.jlipka.swiftrestapi.api.mapper.dto.BankDtoMapper;
import dev.jlipka.swiftrestapi.api.validator.BankValidator;
import dev.jlipka.swiftrestapi.domain.model.Bank;
import dev.jlipka.swiftrestapi.repository.BankRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringBootTest
public class BankEntityServiceIntegrationTest {

    private static final String TEST_EXCEL_FILE = "test.xlsx";

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0").withExposedPorts(27017);
    static MultipartFile testFile;

    @Autowired
    ExcelService<Bank> bankExcelService;
    @Autowired
    private BankEntityService bankEntityService;
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private BankValidator bankValidator;
    @Autowired
    private BankDtoMapper bankDtoMapper;

    @DynamicPropertySource
    static void containersProperties(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
    }

    @BeforeAll
    static void setUpAll() throws IOException {
        Resource resource = new ClassPathResource(TEST_EXCEL_FILE);
        try (InputStream inputStream = resource.getInputStream()) {
            testFile = new MockMultipartFile("test", TEST_EXCEL_FILE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", inputStream);
        }
    }

    @BeforeEach
    void setUp() {
        bankExcelService.importFile(testFile, false);
    }

    @Test
    void findByCountryCode_ShouldReturnCountrySwiftCodes() {
        // Act
        CountrySwiftCodes result = bankEntityService.findByCountryCode("AL");

        // Assert
        assertNotNull(result);
        assertEquals("AL", result.countryISO2());
        assertNotNull(result.swiftCodes());
        assertFalse(result.swiftCodes()
                .isEmpty());
        assertEquals("ALBANIA", result.countryName());
    }

    @AfterEach
    void tearDown() {
        bankRepository.deleteAll();
    }
}