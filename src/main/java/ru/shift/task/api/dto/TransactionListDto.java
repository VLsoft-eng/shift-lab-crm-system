package ru.shift.task.api.dto;

import java.util.List;

public record TransactionListDto(
        List<TransactionDto> transactions) {
}
