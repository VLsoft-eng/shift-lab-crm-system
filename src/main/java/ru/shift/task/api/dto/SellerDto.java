package ru.shift.task.api.dto;

import java.time.LocalDateTime;

public record SellerDto
        (Long id,
         String name,
         String contactInfo,
         LocalDateTime registrationDate) {
}
