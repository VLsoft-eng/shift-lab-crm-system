package ru.shift.task.api.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record TransactionListDto(
        List<TransactionDto> transactions) {
}
