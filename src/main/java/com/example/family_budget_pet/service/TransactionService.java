package com.example.family_budget_pet.service;

import com.example.family_budget_pet.domain.Transaction;
import com.example.family_budget_pet.domain.enums.CategoryType;
import com.example.family_budget_pet.repository.TransactionRepository;
import com.example.family_budget_pet.repository.spec.TransactionSpecification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;

    public List<Transaction> filter(BigDecimal amount,
                                    Long categoryId,
                                    Long userId,
                                    LocalDateTime startDate,
                                    LocalDateTime endDate){
        return repository.findAll(TransactionSpecification.filter(amount, categoryId, userId, startDate, endDate));
    }

    public Double getIncome(Long userId, String type){

    }

}

