package dev.jlipka.swiftrestapi.controller;

import dev.jlipka.swiftrestapi.dto.UploadResponseDto;
import dev.jlipka.swiftrestapi.model.Bank;
import dev.jlipka.swiftrestapi.service.ExcelService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Validated
public class BankUploadController {
    private final ExcelService<Bank> excelService;

    public BankUploadController(ExcelService<Bank> excelService) {
        this.excelService = excelService;
    }

    @PostMapping(value = "/excel/bank/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadResponseDto<Bank> importFile(@RequestParam("file") MultipartFile file,
                                              @RequestParam("has-header-row") boolean hasHeaderRow) {
        return excelService.importFile(file, hasHeaderRow);
    }
}