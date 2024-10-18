package ru.shift.task.api.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record SellerListDto
        (List<SellerDto> sellerDtos) {
}
