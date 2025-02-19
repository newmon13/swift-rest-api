package dev.jlipka.swiftrestapi.api.controller;

import dev.jlipka.swiftrestapi.api.dto.ImportResult;
import dev.jlipka.swiftrestapi.domain.model.Bank;
import dev.jlipka.swiftrestapi.service.ExcelService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BankImportController {
    private final ExcelService<Bank> excelService;

    public BankImportController(ExcelService<Bank> excelService) {
        this.excelService = excelService;
    }

    @PostMapping(value = "/excel/bank/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImportResult importFile(@RequestParam("file") MultipartFile file,
                                         @RequestParam("has_header_row") boolean hasHeaderRow) {
        return excelService.importFile(file, hasHeaderRow);
    }
}