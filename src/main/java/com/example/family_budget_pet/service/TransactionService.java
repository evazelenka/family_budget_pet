package com.example.family_budget_pet.service;

import com.example.family_budget_pet.domain.Category;
import com.example.family_budget_pet.domain.Transaction;
import com.example.family_budget_pet.domain.User;
import com.example.family_budget_pet.domain.enums.CategoryType;
import com.example.family_budget_pet.repository.CategoryRepository;
import com.example.family_budget_pet.repository.TransactionRepository;
import com.example.family_budget_pet.repository.UserRepository;
import com.example.family_budget_pet.repository.spec.TransactionSpecification;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

//    public List<Transaction> filter(BigDecimal amount,
//                                    Long categoryId,
//                                    Long userId,
//                                    LocalDateTime startDate,
//                                    LocalDateTime endDate){
//        return repository.findAll(TransactionSpecification.filter(amount, categoryId, userId, startDate, endDate));
//    }

//    public Double getIncome(Long userId, String type){
//
//    }

    @Transactional
    public void save(Transaction transaction, String type, String description, String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        Category c = categoryRepository.findByName(type);

        transaction.setDate(LocalDateTime.now());
        transaction.setUser(user);
        transaction.setDescription(description);
        transaction.setCategory(c);
        repository.save(transaction);
    }
}

