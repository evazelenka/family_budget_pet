package com.example.family_budget_pet.service;

import com.example.family_budget_pet.domain.Category;
import com.example.family_budget_pet.domain.dto.CategoryStats;
import com.example.family_budget_pet.domain.dto.TypeStats;
import com.example.family_budget_pet.repository.CategoryRepository;
import com.example.family_budget_pet.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

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

    public List<CategoryStats> getFilterStats(String username, String categoryName, LocalDateTime dateStart, LocalDateTime dateEnd) {
        Category c = categoryRepository.findByName(categoryName);
        if (c != null){
            return transactionRepository.findFilteredStats(username, c.getId(), dateStart, dateEnd);
        }
        return transactionRepository.findFilteredStats(username, null, dateStart, dateEnd);
    }
}
