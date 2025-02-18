package dev.jlipka.swiftrestapi.infrastructure.error;

public class BankNotFoundException extends RuntimeException {
  public BankNotFoundException(String message) {
    super(message);
  }
}
