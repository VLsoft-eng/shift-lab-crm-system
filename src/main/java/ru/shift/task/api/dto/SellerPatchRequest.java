package ru.shift.task.api.dto;

import lombok.Builder;

@Builder
public record SellerPatchRequest(
        String name,
        String contactInfo) {
}
