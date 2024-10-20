package ru.shift.task.domain.exception;

public class SellerNotFoundException extends RuntimeException {
    public SellerNotFoundException(Long id) {
        super("Seller with id " + id + " not found");
    }

    public SellerNotFoundException(String message) {
        super(message);
    }
}
