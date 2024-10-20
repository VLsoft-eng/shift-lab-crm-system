package ru.shift.task.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BestSellerInPeriodRequest(
        @NotNull(message = "Start time point is required") LocalDateTime start,
        @Pattern(regexp = "^(DAY|MONTH|QUARTER|YEAR)$", message = "Period must be one of: DAY, MONTH, QUARTER, YEAR")
        String period) {
}
