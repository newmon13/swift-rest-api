package dev.jlipka.swiftrestapi.controller;

import dev.jlipka.swiftrestapi.service.BankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UploadController.class)
class UploadControllerWebMvcTest {

    @MockBean
    private BankService bankService;

    @Autowired
    private MockMvc mockMvc;

    @Value("${app.storage.location}")
    private String storageLocation;

    @BeforeEach
    void setUp() {
        // Ensure the test storage directory exists
        Path uploadDir = Path.of(System.getProperty("java.io.tmpdir"), "test-uploads");
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            fail("Could not create test directory", e);
        }
    }

    @Test
    void shouldUploadASpreadsheetFileAndReturnOk() throws Exception {
        // Create test file content
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "test content".getBytes()
        );

        // Mock service behavior
        doNothing().when(bankService).processFile(any(Path.class).toFile());

        mockMvc.perform(multipart("/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string("File uploaded successfully"));
    }

    @Test
    void shouldReturnBadRequestWhenFileIsNotXlsx() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "test content".getBytes()
        );

        mockMvc.perform(multipart("/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Only .xlsx files are allowed"));
    }
}