package ru.shift.task.api.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TransactionDto(
        Long id,
        Long sellerId,
        Long amount,
        String paymentType,
        LocalDateTime transactionDate) {
}
