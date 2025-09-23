package com.example.family_budget_pet.controllers;

import com.example.family_budget_pet.domain.*;
import com.example.family_budget_pet.domain.dto.CategoryStats;
import com.example.family_budget_pet.domain.dto.TypeStats;
import com.example.family_budget_pet.domain.enums.CategoryType;
import com.example.family_budget_pet.service.CategoryService;
import com.example.family_budget_pet.service.StatsService;
import com.example.family_budget_pet.service.TransactionService;
import com.example.family_budget_pet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;
    private final UserService userService;
    private final CategoryService categoryService;
    @GetMapping("/my")
    public String getMyStats(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model){
        User user = userService.findByUsername(principal.getUsername());
        List<CategoryStats> categoryStats = statsService.getUserStatsByCategory(user.getId());
        if (!categoryStats.isEmpty()){
            List<CategoryStats> expense = categoryStats.stream().filter(c -> categoryService.findByName(c.getCategoryName()).getType().equals(CategoryType.EXPENSE)).toList();
            List<CategoryStats> income = categoryStats.stream().filter(c -> categoryService.findByName(c.getCategoryName()).getType().equals(CategoryType.INCOME)).toList();
            BigDecimal totalExpense = expense.stream().map(CategoryStats::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
            System.out.println("total expense: " + totalExpense);
            BigDecimal totalIncome = income.stream().map(CategoryStats::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
            model.addAttribute("totalExpense", totalExpense);
            model.addAttribute("totalIncome", totalIncome);
            model.addAttribute("categoryStatsExpense", expense);
            model.addAttribute("categoryStatsIncome", income);

        }
        return "general/stats";
    }

    @GetMapping("/group")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_READER')")
    public String getGroupStatsByCategory(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model) {
        User user = userService.findByUsername(principal.getUsername());
        List<CategoryStats> categoryStats = statsService.getGroupStatsByCategory(user.getGroup().getId());
        if (!categoryStats.isEmpty()){
            List<CategoryStats> expense = categoryStats.stream().filter(c -> categoryService.findByName(c.getCategoryName()).getType().equals(CategoryType.EXPENSE)).toList();
            List<CategoryStats> income = categoryStats.stream().filter(c -> categoryService.findByName(c.getCategoryName()).getType().equals(CategoryType.INCOME)).toList();
            BigDecimal totalExpense = expense.stream().map(CategoryStats::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
            System.out.println("total expense: " + totalExpense);
            BigDecimal totalIncome = income.stream().map(CategoryStats::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
            model.addAttribute("totalExpense", totalExpense);
            model.addAttribute("totalIncome", totalIncome);
            model.addAttribute("categoryStatsExpense", expense);
            model.addAttribute("categoryStatsIncome", income);
        }
        return "admin-reader/users-stats";
    }

    @GetMapping("/user/{userId}/by-type")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_READER')")
    public List<TypeStats> getUserStatsByType(@PathVariable Long userId) {
        return statsService.getUserStatsByType(userId);
    }

    @GetMapping("/user/{userId}/by-category")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_READER')")
    public List<CategoryStats> getUserStatsByCategory(@PathVariable Long userId) {
        return statsService.getUserStatsByCategory(userId);
    }

//    @GetMapping("/group/{groupId}/by-type")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_READER')")
//    public List<TypeStats> getGroupStatsByType(@PathVariable Long groupId) {
//        return statsService.getGroupStatsByType(groupId);
//    }
//
}
