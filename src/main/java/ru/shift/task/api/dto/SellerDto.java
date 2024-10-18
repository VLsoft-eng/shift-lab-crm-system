package ru.shift.task.api.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SellerDto
        (Long id,
         String name,
         String contactInfo,
         LocalDateTime registrationDate) {
}
