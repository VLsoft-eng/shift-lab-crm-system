package ru.shift.task.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BestSellersPeriodRequest(
        @NotNull(message = "Seller id is required") @Min(1) Long sellerId,
        @NotNull(message = "Days in period count is required") @Min(1) Long daysInPeriod) {
}
