package dev.jlipka.swiftrestapi.api.mapper.dto;

import dev.jlipka.swiftrestapi.api.dto.BankOffice;
import dev.jlipka.swiftrestapi.api.dto.Branch;
import dev.jlipka.swiftrestapi.api.dto.CountrySwiftCodes;
import dev.jlipka.swiftrestapi.api.dto.Headquarter;
import dev.jlipka.swiftrestapi.api.dto.SwiftCode;
import dev.jlipka.swiftrestapi.domain.model.Bank;
import org.apache.commons.lang3.function.TriFunction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Component
public class BankDtoMapper {
    private final BankOfficeMapper bankOfficeMapper;
    private final HeadquarterMapper headquarterMapper;
    private final BranchMapper branchMapper;
    private final BranchToBankMapper branchToBankMapper;
    private final CountrySwiftCodesMapper countrySwiftCodesMapper;
    private final SwiftCodeMapper swiftCodeMapper;

    public BankDtoMapper(BankOfficeMapper bankOfficeMapper, HeadquarterMapper headquarterMapper, BranchMapper branchMapper, BranchToBankMapper branchToBankMapper, CountrySwiftCodesMapper countrySwiftCodesMapper, SwiftCodeMapper swiftCodeMapper) {
        this.bankOfficeMapper = bankOfficeMapper;
        this.headquarterMapper = headquarterMapper;
        this.branchMapper = branchMapper;
        this.branchToBankMapper = branchToBankMapper;
        this.countrySwiftCodesMapper = countrySwiftCodesMapper;
        this.swiftCodeMapper = swiftCodeMapper;
    }


    public Function<Bank, BankOffice> toBankOffice() {
        return bankOfficeMapper;
    }

    public Function<Bank, Branch> toBranch() {
        return branchMapper;
    }

    public Function<Branch, Bank> fromBranch() {
        return branchToBankMapper;
    }

    public BiFunction<Bank, List<Bank>, Headquarter> toHeadquarter() {
        return headquarterMapper;
    }

    public Function<Bank, SwiftCode> toSwiftCode() {
        return swiftCodeMapper;
    }

    public TriFunction<String, String, List<Bank>, CountrySwiftCodes> toCountrySwiftCodes() {
        return countrySwiftCodesMapper;
    }
}
