package ru.shift.task.domain.mapper;


import org.springframework.stereotype.Component;
import ru.shift.task.api.dto.SellerCreateRequest;
import ru.shift.task.api.dto.SellerDto;
import ru.shift.task.data.entity.Seller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SellerMapper {

    public SellerDto toDto(Seller seller) {
        if (seller == null) return null;

        return SellerDto.builder()
                .id(seller.getId())
                .name(seller.getName())
                .contactInfo(seller.getContactInfo())
                .registrationDate(seller.getRegistrationDate())
                .build();
    }

    public List<SellerDto> toDto(List<Seller> sellers) {
        return sellers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Seller toEntity(SellerCreateRequest sellerDto) {
        if (sellerDto == null) return null;

        return Seller.builder()
                .name(sellerDto.name())
                .contactInfo(sellerDto.contactInfo())
                .registrationDate(LocalDateTime.now())
                .build();
    }
}
