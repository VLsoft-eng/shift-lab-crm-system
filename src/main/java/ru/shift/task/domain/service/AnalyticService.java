package ru.shift.task.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.shift.task.api.dto.*;
import ru.shift.task.data.entity.Seller;
import ru.shift.task.data.entity.Transaction;
import ru.shift.task.data.repository.AnalyticsRepository;
import ru.shift.task.data.repository.SellerRepository;
import ru.shift.task.data.repository.TransactionRepository;
import ru.shift.task.data.repository.impl.AnalyticRepositoryImpl;
import ru.shift.task.domain.exception.InvalidPeriodException;
import ru.shift.task.domain.exception.PeriodNotFoundException;
import ru.shift.task.domain.exception.SellerNotFoundException;
import ru.shift.task.domain.exception.TransactionNotFoundException;
import ru.shift.task.domain.mapper.SellerMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticService {
    private SellerMapper sellerMapper;
    private SellerRepository sellerRepository;
    private TransactionRepository transactionRepository;
    private AnalyticsRepository analyticRepository;

    @Autowired
    public void setSellerMapper(SellerMapper sellerMapper) {
        this.sellerMapper = sellerMapper;
    }

    @Autowired
    public void setAnalyticRepository(AnalyticRepositoryImpl analyticRepository) {
        this.analyticRepository = analyticRepository;
    }

    @Autowired
    public void setSellerRepository(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @Autowired
    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public SellerDto findBestSellerInPeriod(BestSellerInPeriodRequest request) {
        LocalDateTime startDate = request.start().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endDate = calculateEndDate(startDate, request.period());

        if (startDate.isAfter(endDate)) {
            throw new InvalidPeriodException("Start date should be before end date");
        }

        Seller bestSeller = analyticRepository.findBestSellerInPeriod(startDate, endDate);

        if (bestSeller == null) {
            throw new SellerNotFoundException("No Sellers found for the specified period.");
        }

        return sellerMapper.toDto(bestSeller);
    }

    private LocalDateTime calculateEndDate(LocalDateTime startDate, String period) {
        return switch (period.toUpperCase()) {
            case "DAY" -> startDate.plusDays(1).minusSeconds(1);
            case "MONTH" -> startDate.plusMonths(1).minusSeconds(1);
            case "QUARTER" -> startDate.plusMonths(3).minusSeconds(1);
            case "YEAR" -> startDate.plusYears(1).minusSeconds(1);
            default -> throw new InvalidPeriodException("Invalid period. Allowed values: DAY, MONTH, QUARTER, YEAR.");
        };
    }

    public List<SellerDto> findSellersWithSumLessThan(SellersWithSumLessThanRequest request) {
        LocalDateTime start = request.start();
        LocalDateTime end = request.end();
        Long threshold = request.threshold();

        List<Seller> sellers = analyticRepository.findSellersWithSumLess(threshold, start, end);

        if (sellers.isEmpty()) {
            throw new SellerNotFoundException("No sellers found with transaction sums less than the specified threshold.");
        }

        return sellerMapper.toDto(sellers);
    }


    public TimePeriodDto findBestSellersPeriod(BestSellersPeriodRequest request) {
        boolean isSellerExists = sellerRepository.existsById(request.sellerId());
        if (!isSellerExists) {
            throw new SellerNotFoundException(request.sellerId());
        }

        List<Transaction> transactions = transactionRepository.findBySellerId(request.sellerId());
        if (transactions.isEmpty()) {
            throw new TransactionNotFoundException("Not found transactions for current seller.");
        }

        List<Transaction> mutableTransactions = new ArrayList<>(transactions);
        mutableTransactions.sort(Comparator.comparing(Transaction::getTransactionDate));

        Map<LocalDate, Long> dailyTransactionCounts = mutableTransactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTransactionDate().toLocalDate(),
                        Collectors.counting()
                ));

        List<LocalDate> sortedDates = new ArrayList<>(dailyTransactionCounts.keySet());
        long maxCount = 0;
        LocalDate bestStartDate = null;

        Long windowSize = request.daysInPeriod();

        for (int i = 0; i <= sortedDates.size() - windowSize; i++) {
            long currentWindowCount = 0;

            for (int j = i; j < i + windowSize; j++) {
                currentWindowCount += dailyTransactionCounts.get(sortedDates.get(j));
            }

            if (currentWindowCount > maxCount) {
                maxCount = currentWindowCount;
                bestStartDate = sortedDates.get(i);
            }
        }

        if (bestStartDate == null) {
            throw new PeriodNotFoundException("Unable to determine the best period for the seller");
        }

        return TimePeriodDto.builder()
                .start(bestStartDate.atStartOfDay())
                .end(bestStartDate.plusDays(windowSize - 1).atTime(LocalTime.MAX))
                .build();
    }

}
