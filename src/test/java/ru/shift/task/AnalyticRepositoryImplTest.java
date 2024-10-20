package ru.shift.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.shift.task.data.entity.Seller;
import ru.shift.task.data.entity.Transaction;
import ru.shift.task.data.repository.SellerRepository;
import ru.shift.task.data.repository.TransactionRepository;
import ru.shift.task.data.repository.impl.AnalyticRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@EntityScan(basePackages = "ru.shift.task")
public class AnalyticRepositoryImplTest {

    @Autowired
    private AnalyticRepositoryImpl analyticRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setUp() {
        Seller seller = Seller.builder()
                .name("John Doe")
                .contactInfo("john.doe@example.com")
                .registrationDate(LocalDateTime.now())
                .build();

        sellerRepository.save(seller);

        Transaction transaction1 = Transaction.builder()
                .seller(seller)
                .amount(90L)
                .paymentType("CASH")
                .transactionDate(LocalDateTime.now().minusDays(1))
                .build();

        Transaction transaction2 = Transaction.builder()
                .seller(seller)
                .amount(200L)
                .paymentType("CARD")
                .transactionDate(LocalDateTime.now().minusDays(2))
                .build();

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
    }

    @Test
    @DisplayName("Find sellers with sum less than threshold")
    public void testFindSellersWithSumLess() {
        Long threshold = 300L;
        LocalDateTime start = LocalDateTime.now().minusDays(3);
        LocalDateTime end = LocalDateTime.now();

        List<Seller> sellers = analyticRepository.findSellersWithSumLess(threshold, start, end);

        assertThat(sellers).hasSize(1);
    }

    @Test
    @DisplayName("Find best seller in period")
    public void testFindBestSellerInPeriod() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(3);
        LocalDateTime endDate = LocalDateTime.now();

        Seller bestSeller = analyticRepository.findBestSellerInPeriod(startDate, endDate);

        assertThat(bestSeller).isNotNull();
        assertThat(bestSeller.getName()).isEqualTo("John Doe");
    }
}