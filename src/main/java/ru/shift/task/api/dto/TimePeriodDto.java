package ru.shift.task.api.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TimePeriodDto(
        LocalDateTime start,
        LocalDateTime end) {
}
