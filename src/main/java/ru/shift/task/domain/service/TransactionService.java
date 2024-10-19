package ru.shift.task.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.shift.task.api.dto.TransactionCreateRequest;
import ru.shift.task.api.dto.TransactionDto;
import ru.shift.task.data.entity.Transaction;
import ru.shift.task.data.repository.TransactionRepository;
import ru.shift.task.domain.exception.TransactionNotFoundException;
import ru.shift.task.domain.mapper.TransactionMapper;

import java.util.List;

@Service
public class TransactionService {
    TransactionRepository transactionRepository;
    TransactionMapper transactionMapper;

    @Autowired
    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Autowired
    public void setTransactionMapper(TransactionMapper transactionMapper) {
        this.transactionMapper = transactionMapper;
    }

    public void createTransaction(TransactionCreateRequest transactionCreateRequest) {
        transactionRepository.save(transactionMapper.toEntity(transactionCreateRequest));
    }

    public TransactionDto readTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));

        return transactionMapper.toDto(transaction);
    }

    public List<TransactionDto> readAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactionMapper.toDto(transactions);
    }
}
