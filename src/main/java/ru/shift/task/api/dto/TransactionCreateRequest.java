package ru.shift.task.api.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TransactionCreateRequest(
        Long sellerId,
        Long amount,
        @Pattern(regexp = "^(CASH|CARD|TRANSFER)$", message = "Payment type must be one of: CASH, CARD, TRANSFER") String paymentType,
        LocalDateTime transactionDate) {
}
