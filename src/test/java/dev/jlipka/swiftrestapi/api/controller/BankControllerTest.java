package dev.jlipka.swiftrestapi.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jlipka.swiftrestapi.api.dto.BankOffice;
import dev.jlipka.swiftrestapi.api.dto.Branch;
import dev.jlipka.swiftrestapi.api.dto.CountrySwiftCodes;
import dev.jlipka.swiftrestapi.api.dto.Headquarter;
import dev.jlipka.swiftrestapi.api.dto.ImportResult;
import dev.jlipka.swiftrestapi.api.dto.SwiftCode;
import dev.jlipka.swiftrestapi.domain.model.Bank;
import dev.jlipka.swiftrestapi.service.ExcelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BankControllerTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongo = new MongoDBContainer("mongo:7.0");

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ExcelService<Bank> excelService;

    @BeforeEach
    void setUp() throws IOException {
        Resource resource = new ClassPathResource("test.xlsx");
        File file = resource.getFile();

        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", IOUtils.toByteArray(input));

        excelService.importFile(multipartFile, true);
    }

    @ParameterizedTest
    @CsvSource({"PL, 3", "MC, 2", "CL, 5"})
    void shouldFindAllBanksByCountryCode(String countryCode, int expectedCount) {
        ResponseEntity<CountrySwiftCodes> forEntity = restTemplate.getForEntity("/v1/swift-codes/country/" + countryCode, CountrySwiftCodes.class);
        List<SwiftCode> swiftCodes = Objects.requireNonNull(forEntity.getBody())
                .swiftCodes();
        assertThat(swiftCodes.size()).isEqualTo(expectedCount);
    }

    @ParameterizedTest
    @CsvSource({"BKSACLRMXXX, true", "BKSACLRM068, false"})
    void shouldFindBankWithSpecificSwiftCode(String swiftCode, boolean isHeadquarter) {
        ResponseEntity<BankOffice> response = restTemplate.getForEntity(
                "/v1/swift-codes/" + swiftCode,
                BankOffice.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        BankOffice bankOffice = response.getBody();
        assertThat(bankOffice).isNotNull();
        assertThat(bankOffice.getSwiftCode()).isEqualTo(swiftCode);
        assertThat(bankOffice.isHeadquarter()).isEqualTo(isHeadquarter);
    }
}