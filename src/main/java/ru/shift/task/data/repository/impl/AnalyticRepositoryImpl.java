package ru.shift.task.data.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.shift.task.data.entity.Seller;
import ru.shift.task.data.repository.AnalyticsRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Repository
public class AnalyticRepositoryImpl implements AnalyticsRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Seller> findSellersWithSumLess(Long amountThreshold, LocalDateTime start, LocalDateTime end) {
        List<Seller> sellers = entityManager.createQuery(
                        "SELECT t.seller FROM Transaction t " +
                                "WHERE t.transactionDate BETWEEN :start AND :end " +
                                "GROUP BY t.seller " +
                                "HAVING SUM(t.amount) < :amountThreshold", Seller.class)
                .setParameter("amountThreshold", amountThreshold)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();

        if (sellers.isEmpty()) {
            return Collections.emptyList();
        }

        return sellers;
    }

    @Override
    public Seller findBestSellerInPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        List<Seller> sellers = entityManager.createQuery(
                        "SELECT t.seller FROM Transaction t " +
                                "WHERE t.transactionDate >= :startDate AND t.transactionDate <= :endDate " +
                                "GROUP BY t.seller " +
                                "ORDER BY COUNT(t) DESC", Seller.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();

        if (sellers.isEmpty()) {
            return null;
        }

        return sellers.getFirst();
    }
}