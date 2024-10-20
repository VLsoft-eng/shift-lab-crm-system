package ru.shift.task.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import ru.shift.task.api.controller.AnalyticController;
import ru.shift.task.api.dto.*;
import ru.shift.task.domain.service.AnalyticService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AnalyticControllerTest {

    @Mock
    private AnalyticService analyticService;

    @InjectMocks
    private AnalyticController analyticController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Best seller")
    void bestSellerTest() {
        LocalDateTime now = LocalDateTime.now();
        BestSellerInPeriodRequest request = BestSellerInPeriodRequest.builder()
                .start(now.minusDays(7))
                .period("DAY")
                .build();

        SellerDto expectedBestSeller = SellerDto.builder()
                .id(1L)
                .name("Best Seller")
                .build();

        when(analyticService.findBestSellerInPeriod(any(BestSellerInPeriodRequest.class)))
                .thenReturn(expectedBestSeller);

        ResponseEntity<?> response = analyticController.bestSeller(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedBestSeller, response.getBody());
        verify(analyticService, times(1)).findBestSellerInPeriod(request);
    }

    @Test
    @DisplayName("Sellers sum less than")
    void sellersSumLessTest() {
        LocalDateTime now = LocalDateTime.now();
        SellersWithSumLessThanRequest request = SellersWithSumLessThanRequest.builder()
                .start(now.minusDays(30))
                .end(now)
                .threshold(1000L)
                .build();

        List<SellerDto> expectedSellers = Collections.singletonList(
                SellerDto.builder()
                        .id(2L)
                        .name("Seller Under Threshold")
                        .build()
        );

        when(analyticService.findSellersWithSumLessThan(any(SellersWithSumLessThanRequest.class)))
                .thenReturn(expectedSellers);

        ResponseEntity<?> response = analyticController.sellersSumLess(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedSellers, response.getBody());
        verify(analyticService, times(1)).findSellersWithSumLessThan(request);
    }

    @Test
    @DisplayName("Best seller period test")
    void bestSellerPeriodTest() {
        BestSellersPeriodRequest request = BestSellersPeriodRequest.builder()
                .sellerId(789L)
                .daysInPeriod(30L)
                .build();

        TimePeriodDto expectedTimePeriod = TimePeriodDto.builder()
                .start(LocalDateTime.now().minusDays(7))
                .end(LocalDateTime.now())
                .build();

        when(analyticService.findBestSellersPeriod(any(BestSellersPeriodRequest.class)))
                .thenReturn(expectedTimePeriod);

        ResponseEntity<?> response = analyticController.bestSellerPeriod(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedTimePeriod, response.getBody());
        verify(analyticService, times(1)).findBestSellersPeriod(request);
    }
}
