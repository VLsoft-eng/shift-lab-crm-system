package ru.shift.task.api.dto;

import java.time.LocalDateTime;

public record TransactionDto(
        Long id,
        Long sellerId,
        Long amount,
        String paymentType,
        LocalDateTime paymentDate) {
}
