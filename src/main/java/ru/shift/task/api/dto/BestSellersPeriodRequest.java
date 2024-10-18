package ru.shift.task.api.dto;

import lombok.Builder;

@Builder
public record BestSellersPeriodRequest(
        Long sellerId) {
}
