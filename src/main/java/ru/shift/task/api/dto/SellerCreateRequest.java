package ru.shift.task.api.dto;

import lombok.Builder;

@Builder
public record SellerCreateRequest(
        String name,
        String contactInfo) {
}
