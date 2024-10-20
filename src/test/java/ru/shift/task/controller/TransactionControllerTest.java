package ru.shift.task.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.shift.task.api.controller.TransactionController;
import ru.shift.task.api.dto.TransactionCreateRequest;
import ru.shift.task.api.dto.TransactionDto;
import ru.shift.task.domain.service.TransactionService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    private TransactionCreateRequest transactionCreateRequest;
    private Long transactionId;
    private TransactionDto transactionDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        transactionId = 1L;

        transactionCreateRequest = TransactionCreateRequest.builder()
                .sellerId(1L)
                .amount(150L)
                .paymentType("CASH")
                .transactionDate(LocalDateTime.now())
                .build();

        transactionDto = TransactionDto.builder()
                .id(transactionId)
                .sellerId(1L)
                .amount(150L)
                .paymentType("CASH")
                .transactionDate(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Create transaction")
    void createTransactionTest() {
        ResponseEntity<?> response = transactionController.createTransaction(transactionCreateRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Transaction created");

        verify(transactionService, times(1)).createTransaction(transactionCreateRequest);
    }

    @Test
    @DisplayName("Get transaction")
    void getTransactionTest() {
        when(transactionService.readTransaction(transactionId)).thenReturn(transactionDto);

        ResponseEntity<?> response = transactionController.getTransaction(transactionId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(transactionDto);

        verify(transactionService, times(1)).readTransaction(transactionId);
    }

    @Test
    @DisplayName("Get all transactions")
    void getAllTransactionsTest() {
        List<TransactionDto> transactionDtos = List.of(transactionDto);

        when(transactionService.readAllTransactions()).thenReturn(transactionDtos);

        ResponseEntity<?> response = transactionController.getAllTransactions();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(transactionDtos);

        verify(transactionService, times(1)).readAllTransactions();
    }
}
