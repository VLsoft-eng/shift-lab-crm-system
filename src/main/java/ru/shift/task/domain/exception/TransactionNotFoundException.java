package ru.shift.task.domain.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(Long id) {
        super("Transaction with id " + id + " not found");
    }

    public TransactionNotFoundException(String message) {
        super(message);
    }
}
