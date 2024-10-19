package ru.shift.task.api.dto;

import jakarta.validation.constraints.NotBlank;

public record SellerNamePatchRequest(
        @NotBlank(message = "Name must be not null") String name) {
}
