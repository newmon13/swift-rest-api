package dev.jlipka.swiftrestapi.api.controller;


import dev.jlipka.swiftrestapi.api.dto.BankOffice;
import dev.jlipka.swiftrestapi.api.dto.Branch;
import dev.jlipka.swiftrestapi.api.dto.CountrySwiftCodes;
import dev.jlipka.swiftrestapi.service.BankEntityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1/swift-codes")
public class BankController {

    private final BankEntityService bankEntityService;

    public BankController(BankEntityService bankEntityService) {
        this.bankEntityService = bankEntityService;
    }

    @GetMapping("/{swift-code}")
    public BankOffice getBank(@PathVariable("swift-code") String swiftCode) {
        return bankEntityService.findBySwiftCode(swiftCode);
    }

    @GetMapping("/country/{countryISO2code}")
    public CountrySwiftCodes getCountryBanks(@PathVariable String countryISO2code) {
        return bankEntityService.findByCountryCode(countryISO2code);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> registerBank(@Valid @RequestBody Branch bankFullDetailsDto) {
        return bankEntityService.registerBank(bankFullDetailsDto);
    }

    @DeleteMapping("/{swift-code}")
    public Map<String, String> unregisterBank(@PathVariable("swift-code") String swiftCode) {
        return bankEntityService.unregister(swiftCode);
    }
}
