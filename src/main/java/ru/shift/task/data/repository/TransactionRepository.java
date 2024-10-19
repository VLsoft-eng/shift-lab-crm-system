package ru.shift.task.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shift.task.data.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
