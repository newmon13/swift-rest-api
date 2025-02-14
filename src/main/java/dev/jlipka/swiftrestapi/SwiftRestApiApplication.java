package dev.jlipka.swiftrestapi;

import dev.jlipka.swiftrestapi.model.Bank;
import dev.jlipka.swiftrestapi.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
@SpringBootApplication
public class SwiftRestApiApplication {
    @Autowired
    BankRepository bankRepository;

    public static void main(String[] args) {
        SpringApplication.run(SwiftRestApiApplication.class, args);
    }

    @EventListener(ApplicationStartedEvent.class)
    public void onStartup() {
        bankRepository.save(new Bank("","","","","","","","",""));


    }
}
