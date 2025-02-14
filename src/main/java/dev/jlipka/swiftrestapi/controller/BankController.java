package dev.jlipka.swiftrestapi.controller;

import dev.jlipka.swiftrestapi.dto.BankWithBranchesResponseDto;
import dev.jlipka.swiftrestapi.service.BankEntityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankController {
    private final BankEntityService bankEntityService;

    public BankController(BankEntityService bankEntityService) {
        this.bankEntityService = bankEntityService;
    }

    @GetMapping("/v1/swift-codes/{swift-code}")
    public BankWithBranchesResponseDto getBank(@PathVariable("swift-code") String swiftCode) {
        return null;
    }


}
