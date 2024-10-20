package ru.shift.task.data.repository;

import org.springframework.stereotype.Repository;
import ru.shift.task.data.entity.Seller;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnalyticsRepository {
    List<Seller> findSellersWithSumLess(Long amountThreshold, LocalDateTime start, LocalDateTime end);

    Seller findBestSellerInPeriod(LocalDateTime startDate, LocalDateTime endDate);

}
