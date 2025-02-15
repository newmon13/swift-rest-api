package dev.jlipka.swiftrestapi.error;

public class BankNotFoundException extends RuntimeException {
  public BankNotFoundException(String message) {
    super(message);
  }
}
