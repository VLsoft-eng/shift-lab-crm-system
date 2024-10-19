package ru.shift.task.domain.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shift.task.api.dto.TransactionCreateRequest;
import ru.shift.task.api.dto.TransactionDto;
import ru.shift.task.data.entity.Seller;
import ru.shift.task.data.entity.Transaction;
import ru.shift.task.data.repository.SellerRepository;
import ru.shift.task.domain.exception.SellerNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionMapper {


    private SellerRepository sellerRepository;

    @Autowired
    public void setSellerRepository(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public TransactionDto toDto(Transaction transaction) {
        if (transaction == null) return null;

        return TransactionDto.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .transactionDate(transaction.getTransactionDate())
                .paymentType(transaction.getPaymentType())
                .sellerId(transaction.getSeller().getId()).build();
    }

    public List<TransactionDto> toDto(List<Transaction> transactions) {
        if (transactions == null) return null;

        return transactions.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Transaction toEntity(TransactionCreateRequest transactionRequest) {
        if (transactionRequest == null) {
            return null;
        }

        Seller seller = sellerRepository.findById(transactionRequest.sellerId())
                .orElseThrow(() -> new SellerNotFoundException(transactionRequest.sellerId()));

        return Transaction.builder()
                .seller(seller)
                .amount(transactionRequest.amount())
                .paymentType(transactionRequest.paymentType())
                .transactionDate(transactionRequest.transactionDate())
                .build();
    }


}
