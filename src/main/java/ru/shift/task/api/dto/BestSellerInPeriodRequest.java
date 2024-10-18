package ru.shift.task.api.dto;

import java.time.LocalDateTime;

public record BestSellerInPeriodRequest(
        LocalDateTime startTimePoint,
        LocalDateTime endTimePoint) {
}
