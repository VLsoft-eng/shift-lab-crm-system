package ru.shift.task.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.shift.task.api.dto.*;
import ru.shift.task.data.entity.Seller;
import ru.shift.task.data.entity.Transaction;
import ru.shift.task.data.repository.AnalyticsRepository;
import ru.shift.task.data.repository.SellerRepository;
import ru.shift.task.data.repository.TransactionRepository;
import ru.shift.task.domain.exception.InvalidPeriodException;
import ru.shift.task.domain.exception.SellerNotFoundException;
import ru.shift.task.domain.exception.TransactionNotFoundException;
import ru.shift.task.domain.mapper.SellerMapper;
import ru.shift.task.domain.service.AnalyticService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnalyticServiceTest {

    @Mock
    private SellerMapper sellerMapper;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AnalyticsRepository analyticRepository;

    @InjectMocks
    private AnalyticService analyticService;

    private Seller seller;
    private SellerDto sellerDto;
    private List<Transaction> transactions;

    @BeforeEach
    void setUp() {
        seller = Seller.builder()
                .id(1L)
                .name("Test Seller")
                .contactInfo("test@example.com")
                .registrationDate(LocalDateTime.now())
                .build();

        sellerDto = SellerDto.builder()
                .id(1L)
                .name("Test Seller")
                .contactInfo("test@example.com")
                .registrationDate(LocalDateTime.now())
                .build();

        transactions = List.of(
                new Transaction(1L, seller, 500L, "CASH", LocalDateTime.now()),
                new Transaction(2L, seller, 300L, "CARD", LocalDateTime.now().minusDays(1))
        );
    }

    @Test
    @DisplayName("Find Best Seller In Period Test")
    void findBestSellerInPeriodTest() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        BestSellerInPeriodRequest request = BestSellerInPeriodRequest.builder()
                .start(start)
                .period("MONTH")
                .build();

        when(analyticRepository.findBestSellerInPeriod(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(seller);
        when(sellerMapper.toDto(seller)).thenReturn(sellerDto);

        SellerDto result = analyticService.findBestSellerInPeriod(request);

        assertEquals(sellerDto, result);
        verify(analyticRepository, times(1))
                .findBestSellerInPeriod(any(LocalDateTime.class), any(LocalDateTime.class));
        verify(sellerMapper, times(1)).toDto(seller);
    }

    @Test
    @DisplayName("Find Best Seller in Period Throw Exception")
    void findBestSellerInPeriodThrowExceptionTest() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        BestSellerInPeriodRequest request = BestSellerInPeriodRequest.builder()
                .start(start)
                .period("INVALID")
                .build();

        assertThrows(InvalidPeriodException.class, () -> analyticService.findBestSellerInPeriod(request));
    }

    @Test
    @DisplayName("Find Seller with sum Less than")
    void findSellersWithSumLessThan_shouldReturnListOfSellerDtos() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 12, 31, 23, 59);
        SellersWithSumLessThanRequest request = new SellersWithSumLessThanRequest(start, end, 1000L);

        when(analyticRepository.findSellersWithSumLess(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(seller));
        when(sellerMapper.toDto(List.of(seller))).thenReturn(List.of(sellerDto));

        List<SellerDto> result = analyticService.findSellersWithSumLessThan(request);

        assertEquals(1, result.size());
        assertEquals(sellerDto, result.get(0));
        verify(analyticRepository, times(1))
                .findSellersWithSumLess(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("Find Seller with sum Less than Throwing exception")
    void findSellersWithSumLessThan_shouldThrowSellerNotFoundException() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 12, 31, 23, 59);
        SellersWithSumLessThanRequest request = new SellersWithSumLessThanRequest(start, end, 1000L);

        when(analyticRepository.findSellersWithSumLess(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());

        assertThrows(SellerNotFoundException.class, () -> analyticService.findSellersWithSumLessThan(request));
    }

    @Test
    @DisplayName("Find best Sellers period")
    void findBestSellersPeriod_shouldReturnTimePeriodDto() {
        BestSellersPeriodRequest request = BestSellersPeriodRequest.builder()
                .sellerId(1L)
                .daysInPeriod(2L)
                .build();

        when(sellerRepository.existsById(anyLong())).thenReturn(true);
        when(transactionRepository.findBySellerId(anyLong())).thenReturn(transactions);

        TimePeriodDto expectedPeriod = TimePeriodDto.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();

        TimePeriodDto result = analyticService.findBestSellersPeriod(request);

        assertEquals(expectedPeriod.start().toLocalDate(), result.start().toLocalDate());
        assertEquals(expectedPeriod.end().toLocalDate(), result.end().toLocalDate());
        verify(sellerRepository, times(1)).existsById(anyLong());
        verify(transactionRepository, times(1)).findBySellerId(anyLong());
    }


    @Test
    @DisplayName("Find best sellers period throw seller not found exception")
    void findBestSellersPeriod_shouldThrowSellerNotFoundException() {
        BestSellersPeriodRequest request = BestSellersPeriodRequest.builder()
                .sellerId(999L)
                .daysInPeriod(2L)
                .build();

        when(sellerRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(SellerNotFoundException.class, () -> analyticService.findBestSellersPeriod(request));
        verify(sellerRepository, times(1)).existsById(anyLong());
    }

    @Test
    @DisplayName("Find Best Sellers Period throw exception")
    void findBestSellersPeriod_shouldThrowTransactionNotFoundException() {
        BestSellersPeriodRequest request = BestSellersPeriodRequest.builder()
                .sellerId(1L)
                .daysInPeriod(2L)
                .build();

        when(sellerRepository.existsById(anyLong())).thenReturn(true);
        when(transactionRepository.findBySellerId(anyLong())).thenReturn(List.of());

        assertThrows(TransactionNotFoundException.class, () -> analyticService.findBestSellersPeriod(request));
    }
}