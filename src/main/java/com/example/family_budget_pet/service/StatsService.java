package com.example.family_budget_pet.service;

import com.example.family_budget_pet.domain.dto.CategoryStats;
import com.example.family_budget_pet.domain.dto.TypeStats;
import com.example.family_budget_pet.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final TransactionRepository transactionRepository;

    public List<TypeStats> getUserStatsByType(Long userId) {
        return transactionRepository.findUserStatsByType(userId);
    }

    public List<CategoryStats> getUserStatsByCategory(Long userId) {
        return transactionRepository.findUserStatsByCategory(userId);
    }

//    public List<TypeStats> getGroupStatsByType(Long groupId) {
//        return transactionRepository.findGroupStatsByType(groupId);
//    }
//
    public List<CategoryStats> getGroupStatsByCategory(Long groupId) {
        return transactionRepository.findGroupStatsByCategory(groupId);
    }
}
