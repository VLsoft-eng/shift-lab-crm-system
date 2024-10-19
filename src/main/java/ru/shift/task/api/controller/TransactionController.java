package ru.shift.task.api.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shift.task.api.dto.TransactionCreateRequest;
import ru.shift.task.domain.service.TransactionService;

import static ru.shift.task.api.Paths.TRANSACTIONS;

@RestController
@RequestMapping(TRANSACTIONS)
public class TransactionController {

    TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTransaction(@RequestBody @Valid TransactionCreateRequest transactionCreateRequest) {
        transactionService.createTransaction(transactionCreateRequest);
        return ResponseEntity.ok("Transaction created");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransaction(@PathVariable @Valid @Min(value = 1) Long id) {
        return ResponseEntity.ok(transactionService.readTransaction(id));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTransactions() {
        return ResponseEntity.ok(transactionService.readAllTransactions());
    }
}
