package ru.shift.task.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shift.task.data.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}
