package dev.jlipka.swiftrestapi.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import dev.jlipka.swiftrestapi.mapper.BankMapper;
import dev.jlipka.swiftrestapi.mapper.EntityExtractor;
import dev.jlipka.swiftrestapi.mapper.RowMapper;
import dev.jlipka.swiftrestapi.model.Bank;
import dev.jlipka.swiftrestapi.validator.FileValidator;
import dev.jlipka.swiftrestapi.validator.XlsxValidator;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.text.html.parser.Entity;

@EnableEncryptableProperties
@Configuration
public class SwiftRestApiConfig {

}
