package ru.shift.task.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SellerCreateRequest(

        @NotBlank(message = "Name must be not null") String name,
        @NotBlank(message = "Contact info must be not null") String contactInfo) {
}
