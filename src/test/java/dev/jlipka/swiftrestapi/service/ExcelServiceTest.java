package dev.jlipka.swiftrestapi.service;

import dev.jlipka.swiftrestapi.api.dto.Branch;
import dev.jlipka.swiftrestapi.api.dto.CountrySwiftCodes;
import dev.jlipka.swiftrestapi.api.dto.Headquarter;
import dev.jlipka.swiftrestapi.domain.model.Bank;
import dev.jlipka.swiftrestapi.infrastructure.error.BankNotFoundException;
import dev.jlipka.swiftrestapi.infrastructure.error.ValidationException;
import dev.jlipka.swiftrestapi.repository.BankRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExcelServiceTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @Autowired
    BankRepository bankRepository;

    @Autowired
    BankEntityService bankEntityService;

    @Autowired
    ExcelService<Bank> excelService;

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


    @Test
    void shouldFindBanksByCountryCodeWhenPassedValidAndExistingCountryCode() {
        //given
        String countryCode = "PL";
        //when
        CountrySwiftCodes byCountryCode = bankEntityService.findByCountryCode(countryCode);
        //then
        assertThat(byCountryCode.countryISO2()).isEqualTo(countryCode);
        byCountryCode.swiftCodes()
                .forEach(bank -> assertThat(bank.getCountryISO2()).isEqualTo(countryCode));
    }

    @Test
    void shouldThrowValidationExceptionWhenCountryCodeIsInvalid() {
        //given
        String countryCode = "INVALID CODE";
        //when & then
        assertThrows(ValidationException.class, () -> bankEntityService.findByCountryCode(countryCode));
    }

    @Test
    void shouldThrowValidationExceptionWhenCountryCodeDoesNotExist() {
        //given
        String countryCode = "AA";
        //when & then
        assertThrows(ValidationException.class, () -> bankEntityService.findByCountryCode(countryCode));
    }

    @Test
    void shouldFindBranchBankBySwiftCode() {
        //given
        String swiftCode = "BKSACLRM068";
        //when
        Branch bySwiftCode = bankEntityService.findBySwiftCode(swiftCode);
        //then
        assertThat(bySwiftCode.getSwiftCode()).isEqualTo(swiftCode);
    }

    @Test
    void shouldFindHeadquarterBankBySwiftCode() {
        //given
        String swiftCode = "BKSACLRMXXX";
        //when
        Headquarter bySwiftCode = (Headquarter) bankEntityService.findBySwiftCode(swiftCode);
        //then
        assertThat(bySwiftCode.getSwiftCode()).isEqualTo(swiftCode);
        assertThat(bySwiftCode.getBranches()).isNotEmpty();
        bySwiftCode.getBranches()
                .forEach(bank -> assertThat(bank.getSwiftCode()).containsSubsequence(swiftCode.substring(0,8)));
    }

    @Test
    void shouldThrowValidationExceptionWhenSwiftCodeIsInvalid() {
        //given
        String swiftCode = "INVALID SWIFT CODE";
        //when & then
        assertThrows(ValidationException.class, () -> bankEntityService.findBySwiftCode(swiftCode));
    }

    @Test
    void shouldRegisterCompletelyNewBranchBank() {
        //given
        Branch newBank = getExampleBranchBank();
        //when
        bankEntityService.registerBank(newBank);
        //then
        Branch bySwiftCode = bankEntityService.findBySwiftCode(newBank.getSwiftCode());
        assertThat(bySwiftCode).usingRecursiveComparison().isEqualTo(newBank);
    }

    @Test
    void shouldRegisterCompletelyNewHeadquarterBank() {
        //given
        Branch newBank = getExampleBranchBank();
        newBank.setSwiftCode("AAAAPLAAXXX");
        newBank.setHeadquarter(true);
        //when
        bankEntityService.registerBank(newBank);
        //then
        Headquarter bySwiftCode = (Headquarter) bankEntityService.findBySwiftCode(newBank.getSwiftCode());
        assertThat(bySwiftCode)
                .usingRecursiveComparison()
                .comparingOnlyFields("swiftCode", "bankName", "address", "countryISO2", "countryName", "isHeadquarter")
                .isEqualTo(newBank);
    }

    @Test
    void shouldAddNewBranchBankAndThenAssignToItExistingHeadquarter() {
        //given
        Branch headquarter = getExampleBranchBank();
        headquarter.setSwiftCode("AAAAPLAAXXX");
        headquarter.setHeadquarter(true);
        bankEntityService.registerBank(headquarter);
        Branch branch = getExampleBranchBank();
        bankEntityService.registerBank(branch);
        //when
        Headquarter bySwiftCode = (Headquarter) bankEntityService.findBySwiftCode(headquarter.getSwiftCode());
        //then
        assertThat(bySwiftCode.getBranches()).isNotEmpty();
        assertThat(bySwiftCode.getBranches().stream().anyMatch(
                swiftCode -> swiftCode.getSwiftCode().equals(branch.getSwiftCode()))).isTrue();
        assertThat(bankRepository.findBySwiftCode(branch.getSwiftCode()).get().getHeadquarter().getSwiftCode())
                .isEqualTo(headquarter.getSwiftCode());
    }

    @Test
    void shouldUnregisterExistingBranchBank() {
        //given
        Branch branch = getExampleBranchBank();
        bankEntityService.registerBank(branch);
        //when
        Map<String, String> result = bankEntityService.unregister(branch.getSwiftCode());
        //then
        assertThat(result.get("message")).isEqualTo("Bank deleted successfully");
        assertThrows(BankNotFoundException.class, () -> bankEntityService.findBySwiftCode(branch.getSwiftCode()));
        assertThat(bankRepository.findBySwiftCode(branch.getSwiftCode())).isEmpty();
    }

    @Test
    void shouldThrowBankNotFoundExceptionWhenBankDoesNotExist() {
        //when & then
        assertThrows(BankNotFoundException.class, () -> bankEntityService.unregister("AAAAPLAAAAA"));
    }

    @Test
    void shouldUnregisterHeadquarterAndUpdateItsExistingBranches() {
        //given
        Branch headquarter = getExampleBranchBank();
        headquarter.setSwiftCode("AAAAPLAAXXX");
        headquarter.setHeadquarter(true);
        bankEntityService.registerBank(headquarter);
        Branch branch = getExampleBranchBank();
        bankEntityService.registerBank(branch);
        //when
        Map<String, String> unregister = bankEntityService.unregister(headquarter.getSwiftCode());
        //then
        assertThat(unregister.get("message")).isEqualTo("Bank deleted successfully");
        assertThat(bankRepository.findBySwiftCode(branch.getSwiftCode()).get().getHeadquarter()).isNull();
    }

    private Branch getExampleBranchBank() {
        return Branch.builder()
                .countryISO2("PL")
                .countryName("POLAND")
                .bankName("TEST BANK NAME")
                .swiftCode("AAAAPLAAAAA")
                .address("TEST BANK ADDRESS")
                .isHeadquarter(false)
                .build();
    }


}