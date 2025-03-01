package dev.jlipka.swiftrestapi.api.controller;

import dev.jlipka.swiftrestapi.api.dto.ImportResult;
import dev.jlipka.swiftrestapi.domain.model.Bank;
import dev.jlipka.swiftrestapi.service.ExcelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BankImportController.class)
class BankImportControllerTest {

    @MockitoBean
    private ExcelService<Bank> excelService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldUploadASpreadsheetFileWithValidDataAndReturnOk() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "test content".getBytes());

        ImportResult expectedResponse = new ImportResult(0, 0, emptyList());

        when(excelService.importFile(any(), eq(true))).thenReturn(expectedResponse);

        mockMvc.perform(multipart("/excel/bank/import").file(file)
                        .param("has_header_row", "true")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.entities_in_file").exists())
                .andExpect(jsonPath("$.saved_entities").exists())
                .andExpect(jsonPath("$.failed_imports").isArray())
                .andExpect(jsonPath("$.failed_imports").isEmpty());
    }

    @Test
    void shouldReturnBadRequestWhenFileIsNotXlsx() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());

        mockMvc.perform(multipart("/excel/bank/import").file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

}