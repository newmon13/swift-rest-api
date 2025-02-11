package dev.jlipka.swiftrestapi.controller;

import dev.jlipka.swiftrestapi.repository.BankRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BankController.class)
class BankControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BankRepository bankRepository;

    @Test
    public void shouldUploadASpreadsheetFileAndReturnOk() throws Exception {
        //given
        MockMultipartFile testMockedFile = new MockMultipartFile("file", "filename.xlsx", "application/vnd.ms-excel", "some xlsx".getBytes());

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload").file(testMockedFile))
                .andExpect(status().isOk())
                .andExpect(content().string("File uploaded successfully"));
    }

    @Test
    public void shouldReturnBadRequestWhenUploadedIsNotXLSX() throws Exception {
        //given
        MockMultipartFile testMockedFile = new MockMultipartFile("file", "filename.txt", "text/plain", "some txt".getBytes());

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload").file(testMockedFile))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Uploaded file is not a spreadsheet"));

    }

}