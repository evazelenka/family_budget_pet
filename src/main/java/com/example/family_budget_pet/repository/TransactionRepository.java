package com.example.family_budget_pet.repository;

import com.example.family_budget_pet.domain.Transaction;
import com.example.family_budget_pet.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    List<Transaction> findByUser(User user);
    List<Transaction> findByUserAndDateBetween(User user, LocalDateTime start, LocalDateTime end);
    List<Transaction> findByUserAndCategoryId(User user, Long categoryId);
    List<Transaction> findByCategoryIdAndDateBetween();

}
