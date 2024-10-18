package ru.shift.task.api.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BestSellerInPeriodRequest(
        LocalDateTime startTimePoint,
        LocalDateTime endTimePoint) {
}
