package dev.jlipka.swiftrestapi.api.controller;


import dev.jlipka.swiftrestapi.api.dto.*;
import dev.jlipka.swiftrestapi.service.BankEntityService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/swift-codes")
public class BankController {
    private final BankEntityService bankEntityService;

    public BankController(BankEntityService bankEntityService) {
        this.bankEntityService = bankEntityService;
    }

    @GetMapping("/{swift-code}")
    public BankResponseDto getBank(@PathVariable("swift-code") String swiftCode) {
        return bankEntityService.findBySwiftCode(swiftCode);
    }

    @GetMapping("/country/{countryISO2code}")
    public CountryWithBanksResponseDto getCountryBanks(@PathVariable String countryISO2code) {
        return bankEntityService.findByCountryCode(countryISO2code);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CrudOperationResponseDto registerBank(@RequestBody BranchBankFullDetailsDto branchBankFullDetailsDto) {
        return bankEntityService.registerBank(branchBankFullDetailsDto);
    }

    @DeleteMapping("/{swift-code}")
    public CrudOperationResponseDto unregisterBank(@PathVariable("swift-code") String swiftCode) {
        return bankEntityService.unregister(swiftCode);
    }
}
