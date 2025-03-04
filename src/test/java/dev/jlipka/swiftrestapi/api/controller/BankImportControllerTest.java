package dev.jlipka.swiftrestapi.api.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BankImportControllerTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongo = new MongoDBContainer("mongo:7.0");

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldImportExampleFileWithoutFailedImports() {
        var multipart = new LinkedMultiValueMap<>();
        multipart.add("file", file("test.xlsx"));
        multipart.add("has_header_row", "true");
        final ResponseEntity<Map> response = restTemplate.postForEntity("/excel/bank/import", new HttpEntity<>(multipart, headers()), Map.class);
        Integer savedEntities = (Integer) response.getBody()
                .get("saved_entities");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(savedEntities).isEqualTo(12);
    }

    @Test
    void shouldImportFileWithFailedImports() {
        var multipart = new LinkedMultiValueMap<>();
        multipart.add("file", file("test_with_invalid_banks.xlsx"));
        multipart.add("has_header_row", "true");
        final ResponseEntity<Map> response = restTemplate.postForEntity("/excel/bank/import", new HttpEntity<>(multipart, headers()), Map.class);
        Integer savedEntities = (Integer) response.getBody()
                .get("saved_entities");
        List<?> failedImports = (List<?>) response.getBody()
                .get("failed_imports");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(savedEntities).isEqualTo(9);
        assertThat(failedImports.size()).isEqualTo(3);

    }

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return headers;
    }

    private FileSystemResource file(String fileName) {
        return new FileSystemResource(Path.of("src", "test", "resources", fileName));
    }
}