package com.example.family_budget_pet.controllers;

import com.example.family_budget_pet.domain.*;
import com.example.family_budget_pet.domain.dto.CategoryStats;
import com.example.family_budget_pet.domain.dto.TypeStats;
import com.example.family_budget_pet.domain.enums.CategoryType;
import com.example.family_budget_pet.service.CategoryService;
import com.example.family_budget_pet.service.GroupService;
import com.example.family_budget_pet.service.StatsService;
import com.example.family_budget_pet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final GroupService groupService;

    @GetMapping("/my")
    public String getMyStats(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model){
        User user = userService.findByUsername(principal.getUsername());
        List<CategoryStats> categoryStats = statsService.getUserStatsByCategory(user.getId());
        if (!categoryStats.isEmpty()){
            List<CategoryStats> expense = statsService.getExpenseFromCategoryStats(categoryStats);
            List<CategoryStats> income = statsService.getIncomeFromCategoryStats(categoryStats);
            BigDecimal totalExpense = statsService.getTotalFromCategoryStats(expense);
            BigDecimal totalIncome = statsService.getTotalFromCategoryStats(income);

            model.addAttribute("totalExpense", totalExpense);
            model.addAttribute("totalIncome", totalIncome);
            model.addAttribute("categoryStatsExpense", expense);
            model.addAttribute("categoryStatsIncome", income);
        }
        return "general/stats";
    }

    @GetMapping("/my/filter")
    public String getFilterPage(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model){
        model.addAttribute("title", "Статистика");
        return "general/stats-filter";
    }

    @PostMapping("/my/filter")
    public String getFilterStats(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                                 Model model,
                                 @RequestParam(required = false) String categoryName,
                                 @RequestParam(required = false) LocalDateTime dateStart,
                                 @RequestParam(required = false) LocalDateTime dateEnd){

        if (dateStart != null && dateEnd != null){
            if (dateStart.isAfter(dateEnd)) {
    //            throw new IllegalArgumentException();
                model.addAttribute("error", "Дата начала не может быть позже даты окончания");
                return "general/stats-filter";
            }
            if (dateStart.isAfter(LocalDateTime.now()) || dateEnd.isAfter(LocalDateTime.now())) {
    //            throw new IllegalArgumentException();
                model.addAttribute("error", "Даты не могут быть позже текущего времени");
                return "general/stats-filter";
            }
        }
        String parameters = statsService.getParametersString(categoryName, dateStart, dateEnd);
        List<CategoryStats> categoryStats = statsService.getCategoryStats(categoryName, principal.getUsername(), dateStart, dateEnd);

        if (categoryStats != null){
            List<CategoryStats> expense = statsService.getExpenseFromCategoryStats(categoryStats);
            List<CategoryStats> income = statsService.getIncomeFromCategoryStats(categoryStats);

            BigDecimal totalExpense = statsService.getTotalFromCategoryStats(expense);
            BigDecimal totalIncome = statsService.getTotalFromCategoryStats(income);

            model.addAttribute("parameters", parameters);
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
        Group group = user.getGroup();
        if (group != null){
            List<CategoryStats> categoryStats = statsService.getGroupStatsByCategory(group.getId());
            if (!categoryStats.isEmpty()){
                List<CategoryStats> expense = statsService.getExpenseFromCategoryStats(categoryStats);
                List<CategoryStats> income = statsService.getIncomeFromCategoryStats(categoryStats);

                BigDecimal totalExpense = statsService.getTotalFromCategoryStats(expense);
                BigDecimal totalIncome = statsService.getTotalFromCategoryStats(income);

//                model.addAttribute("parameters", parameters);
                model.addAttribute("totalExpense", totalExpense);
                model.addAttribute("totalIncome", totalIncome);
                model.addAttribute("categoryStatsExpense", expense);
                model.addAttribute("categoryStatsIncome", income);
            }
        }
        model.addAttribute("title", "Статистика");
        return "admin-reader/users-stats";
    }

    @GetMapping("/group/filter")
    public String getGroupFilterPage(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model){
        model.addAttribute("title", "Статистика");
        User user = userService.findByUsername(principal.getUsername());
        if (user.getGroup() == null) model.addAttribute("error", "Группа не найдена");
        else model.addAttribute("users", user.getGroup().getUsers());

        return "admin-reader/stats-group-filter";
    }

    @PostMapping("/group/filter")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_READER')")
    public String getGroupStatByCategory(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model,
                                         @RequestParam(required = false) String categoryName,
                                         @RequestParam(required = false) String username,
                                         @RequestParam(required = false) LocalDateTime dateStart,
                                         @RequestParam(required = false) LocalDateTime dateEnd) {
        if (dateStart != null && dateEnd != null){
            if (dateStart.isAfter(dateEnd)) {
                //            throw new IllegalArgumentException();
                model.addAttribute("error", "Дата начала не может быть позже даты окончания");
                return "general/stats-filter";
            }
            if (dateStart.isAfter(LocalDateTime.now()) || dateEnd.isAfter(LocalDateTime.now())) {
                //            throw new IllegalArgumentException();
                model.addAttribute("error", "Даты не могут быть позже текущего времени");
                return "general/stats-filter";
            }
        }
        List<CategoryStats> categoryStats = new ArrayList<>();
        String parameters = statsService.getParametersString(categoryName, dateStart, dateEnd);
        if (!username.equals("-")){
            categoryStats = statsService.getCategoryStats(categoryName, username, dateStart, dateEnd);
            parameters = parameters + " Пользователь: " + username;
        } else {
            User user = userService.findByUsername(principal.getUsername());
            Long groupId = user.getGroup().getId();
            categoryStats = statsService.getGroupCategoryStats(categoryName, groupId, dateStart, dateEnd);
        }
        if (categoryStats != null){
            List<CategoryStats> expense = statsService.getExpenseFromCategoryStats(categoryStats);
            List<CategoryStats> income = statsService.getIncomeFromCategoryStats(categoryStats);

            BigDecimal totalExpense = statsService.getTotalFromCategoryStats(expense);
            BigDecimal totalIncome = statsService.getTotalFromCategoryStats(income);

            model.addAttribute("parameters", parameters);
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
}