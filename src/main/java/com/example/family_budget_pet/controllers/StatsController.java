package com.example.family_budget_pet.controllers;

import com.example.family_budget_pet.domain.*;
import com.example.family_budget_pet.domain.dto.CategoryStats;
import com.example.family_budget_pet.domain.dto.TypeStats;
import com.example.family_budget_pet.service.StatsService;
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
import java.util.concurrent.atomic.AtomicReference;

@Controller
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;
    private final UserService userService;

    @GetMapping("/my")
    public String getMyStats(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model){
        User user = userService.findByUsername(principal.getUsername());
        List<CategoryStats> categoryStats = statsService.getUserStatsByCategory(user.getId());
        if (!categoryStats.isEmpty()){
            List<CategoryStats> expense = categoryStats.stream().filter(c -> c.getCategoryName().equals("Дом") || c.getCategoryName().equals("Здоровье") || c.getCategoryName().equals("Еда")).toList();
            List<CategoryStats> income = categoryStats.stream().filter(c -> c.getCategoryName().equals("Зарплата") || c.getCategoryName().equals("Пассивный доход") || c.getCategoryName().equals("Возврат долгов")).toList();

//            model.addAttribute("totalExpense", totalExpense);
//            model.addAttribute("totalIncome", totalIncome);
            model.addAttribute("categoryStatsExpense", expense);
            model.addAttribute("categoryStatsIncome", income);

        }
        return "general/stats";
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
//    @GetMapping("/group/{groupId}/by-category")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_READER')")
//    public List<CategoryStats> getGroupStatsByCategory(@PathVariable Long groupId) {
//        return statsService.getGroupStatsByCategory(groupId);
//    }
}
