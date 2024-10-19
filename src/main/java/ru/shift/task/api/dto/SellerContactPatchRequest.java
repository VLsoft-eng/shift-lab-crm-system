package ru.shift.task.api.dto;

import jakarta.validation.constraints.NotBlank;

public record SellerContactPatchRequest(
        @NotBlank(message = "Contact info must be not null") String contactInfo) {
}
