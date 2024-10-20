package ru.shift.task.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.shift.task.api.dto.TransactionCreateRequest;
import ru.shift.task.api.dto.TransactionDto;
import ru.shift.task.data.entity.Transaction;
import ru.shift.task.data.repository.TransactionRepository;
import ru.shift.task.domain.exception.TransactionNotFoundException;
import ru.shift.task.domain.mapper.TransactionMapper;
import ru.shift.task.domain.service.TransactionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Create transaction")
    void createTransactionTest() {
        TransactionCreateRequest request = TransactionCreateRequest.builder()
                .sellerId(1L)
                .amount(150L)
                .paymentType("CASH")
                .transactionDate(LocalDateTime.now())
                .build();

        Transaction transactionEntity = Transaction.builder()
                .amount(request.amount())
                .paymentType(request.paymentType())
                .transactionDate(request.transactionDate())
                .build();

        when(transactionMapper.toEntity(any(TransactionCreateRequest.class))).thenReturn(transactionEntity);

        transactionService.createTransaction(request);

        verify(transactionRepository, times(1)).save(transactionEntity);
    }

    @Test
    @DisplayName("Read transaction")
    void readTransactionTest() {
        Transaction transactionEntity = Transaction.builder()
                .id(1L)
                .amount(100L)
                .paymentType("CARD")
                .transactionDate(LocalDateTime.now())
                .build();

        TransactionDto transactionDto = TransactionDto.builder()
                .id(transactionEntity.getId())
                .sellerId(1L)
                .amount(transactionEntity.getAmount())
                .paymentType(transactionEntity.getPaymentType())
                .transactionDate(transactionEntity.getTransactionDate())
                .build();

        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transactionEntity));
        when(transactionMapper.toDto(any(Transaction.class))).thenReturn(transactionDto);

        TransactionDto result = transactionService.readTransaction(1L);

        assertEquals(transactionDto, result);
        verify(transactionRepository, times(1)).findById(1L);
        verify(transactionMapper, times(1)).toDto(transactionEntity);
    }

    @Test
    @DisplayName("Read transaction and throw exception")
    void readTransactionThrowExceptionTest() {
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.readTransaction(1L));
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Read all transactions")
    void readAllTransactionsTest() {
        List<Transaction> transactions = List.of(
                Transaction.builder().id(1L).amount(100L).paymentType("CASH").transactionDate(LocalDateTime.now()).build(),
                Transaction.builder().id(2L).amount(200L).paymentType("CARD").transactionDate(LocalDateTime.now()).build()
        );

        List<TransactionDto> transactionDtos = List.of(
                TransactionDto.builder().id(1L).sellerId(1L).amount(100L).paymentType("CASH").transactionDate(LocalDateTime.now()).build(),
                TransactionDto.builder().id(2L).sellerId(2L).amount(200L).paymentType("CARD").transactionDate(LocalDateTime.now()).build()
        );

        when(transactionRepository.findAll()).thenReturn(transactions);
        when(transactionMapper.toDto(transactions)).thenReturn(transactionDtos);

        List<TransactionDto> result = transactionService.readAllTransactions();

        assertEquals(2, result.size());
        assertEquals(transactionDtos, result);
        verify(transactionRepository, times(1)).findAll();
        verify(transactionMapper, times(1)).toDto(transactions);
    }
}
