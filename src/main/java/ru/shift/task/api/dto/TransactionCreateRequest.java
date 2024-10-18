package ru.shift.task.api.dto;

import java.time.LocalDateTime;

public record TransactionCreateRequest(
        Long sellerId,
        Long amount,
        String paymentType,
        LocalDateTime transactionDate) {
}
