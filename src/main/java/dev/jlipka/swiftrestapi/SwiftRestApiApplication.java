package dev.jlipka.swiftrestapi;

import dev.jlipka.swiftrestapi.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SwiftRestApiApplication {
    @Autowired
    BankRepository bankRepository;

    public static void main(String[] args) {
        SpringApplication.run(SwiftRestApiApplication.class, args);
    }

}