package dev.jlipka.swiftrestapi.api.controller;

import dev.jlipka.swiftrestapi.api.dto.BankOffice;
import dev.jlipka.swiftrestapi.api.dto.Branch;
import dev.jlipka.swiftrestapi.api.dto.CountrySwiftCodes;
import dev.jlipka.swiftrestapi.api.dto.Headquarter;
import dev.jlipka.swiftrestapi.api.dto.SwiftCode;
import dev.jlipka.swiftrestapi.domain.model.Bank;
import dev.jlipka.swiftrestapi.repository.BankRepository;
import dev.jlipka.swiftrestapi.service.ExcelService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Autowired
    BankRepository bankRepository;

    @BeforeEach
    void setUp() throws IOException {
        Resource resource = new ClassPathResource("test.xlsx");
        File file = resource.getFile();

        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", IOUtils.toByteArray(input));

        excelService.importFile(multipartFile, true);
    }

    @AfterEach
    void tearDown() {
        bankRepository.deleteAll();
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
        ResponseEntity<BankOffice> response = restTemplate.getForEntity("/v1/swift-codes/" + swiftCode, BankOffice.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        BankOffice bankOffice = response.getBody();
        assertThat(bankOffice).isNotNull();
        assertThat(bankOffice.getSwiftCode()).isEqualTo(swiftCode);
        assertThat(bankOffice.isHeadquarter()).isEqualTo(isHeadquarter);
    }

    @Test
    void shouldRegisterNewBranchBankWithoutExistingHeadquarter() {
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/v1/swift-codes",
                getTestBranchBank(),
                Map.class
        );
        Map<String, String> responseMap = response.getBody();
        String value = responseMap.get("message");
        assertThat(value).isEqualTo("Bank created successfully");
    }

    @Test
    void shouldReturnBadRequestAndDetailedMessageWhenCountryCodeNotExists() {
        Branch testBranchBank = getTestBranchBank();
        testBranchBank.setCountryISO2("AA");
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/v1/swift-codes",
                testBranchBank,
                Map.class
        );
        Map<String, String> responseMap = response.getBody();
        String status = responseMap.get("status");
        String message = responseMap.get("message");
        assertThat(status).isEqualTo("BAD_REQUEST");
        assertThat(message).isEqualTo("No country with given country code was found. Check country code and relevant part of SWIFT code");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    void shouldReturnHeadquarterWithOneBranch() {
        Branch headquarter = getTestBranchBank();
        headquarter.setSwiftCode("AAAAPLAAXXX");
        headquarter.setHeadquarter(true);
        Branch branch = getTestBranchBank();

        restTemplate.postForEntity("/v1/swift-codes", headquarter, Map.class);
        restTemplate.postForEntity("/v1/swift-codes", branch, Map.class);

        ResponseEntity<Headquarter> headquarterResponse = restTemplate.getForEntity(
                "/v1/swift-codes/" + headquarter.getSwiftCode(),
                Headquarter.class
        );
        Headquarter body = headquarterResponse.getBody();
        assertThat(body.getBranches()).isNotEmpty();
        assertThat(headquarterResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldUnregisterBank() {
        String swiftCode = "BIGBPLPWCUS";
        ResponseEntity<Map> exchange = restTemplate.exchange("/v1/swift-codes/" + swiftCode, HttpMethod.DELETE, HttpEntity.EMPTY, Map.class);
        String message = (String) exchange.getBody()
                .get("message");
        assertThat(message).isEqualTo("Bank deleted successfully");
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldReturnNotFoundWhenBankForDeletionDoesNotExist() {
        String swiftCode = "AAAAPLAAAAA";
        ResponseEntity<Map> exchange = restTemplate.exchange("/v1/swift-codes/" + swiftCode, HttpMethod.DELETE, HttpEntity.EMPTY, Map.class);
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }



    private Branch getTestBranchBank() {
        Branch testRegisterNewBankRequest = new Branch();
        testRegisterNewBankRequest.setAddress("Test address");
        testRegisterNewBankRequest.setBankName("Test bank name");
        testRegisterNewBankRequest.setCountryISO2("PL");
        testRegisterNewBankRequest.setCountryName("POLAND");
        testRegisterNewBankRequest.setHeadquarter(false);
        testRegisterNewBankRequest.setSwiftCode("AAAAPLAAAAA");
        return testRegisterNewBankRequest;
    }
}