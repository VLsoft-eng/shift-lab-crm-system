package ru.shift.task.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SellersWithSumLessThanRequest(
        @NotNull(message = "Start time point is required") LocalDateTime start,
        @NotNull(message = "End time point is required") LocalDateTime end,
        @NotNull(message = "Threshold is required") Long threshold) {
}
