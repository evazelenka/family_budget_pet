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

    @Transactional
    public Transaction save(Transaction transaction, String type, String description, String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        Category c = categoryRepository.findByName(type);

        System.out.println("-----------------------------------");
        System.out.println(c.getName());
        System.out.println("-----------------------------------");
        transaction.setDate(LocalDateTime.now());
        transaction.setUser(user);
        transaction.setDescription(description);
        transaction.setCategory(c);
        return repository.save(transaction);
    }

    @Transactional
    public Transaction update(Transaction t, String type){
        Transaction tran = findById(t.getId());
        Category c = categoryRepository.findByName(type);
        t.setCategory(c);
        if (t.getAmount() != null){
            tran.setAmount(t.getAmount());
        }
        if (t.getDate() != null){
            tran.setDate(t.getDate());
        }
        if (t.getCategory() != null){
            tran.setCategory(t.getCategory());
        }
        if (t.getDescription() != null){
            tran.setDescription(t.getDescription());
        }
        return repository.save(tran);
    }

    @Transactional
    public void deleteById(Long id){
        repository.deleteById(id);
    }

    public Transaction findById(Long id){
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public Transaction updateTransDescription(Long id, String newDescription){
        Transaction t = findById(id);
        if (t == null){
            return null;
        }
        t.setDescription(newDescription);
        repository.save(t);
        return t;
    }

    public List<Transaction> findAllByGroupId(Long groupId){
        return repository.findAllByGroupId(groupId);
    }
}

