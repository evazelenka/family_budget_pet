package com.example.family_budget_pet.controllers;

import com.example.family_budget_pet.domain.dto.CategoryStats;
import com.example.family_budget_pet.domain.dto.TypeStats;
import com.example.family_budget_pet.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

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

    @GetMapping("/group/{groupId}/by-type")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_READER')")
    public List<TypeStats> getGroupStatsByType(@PathVariable Long groupId) {
        return statsService.getGroupStatsByType(groupId);
    }

    @GetMapping("/group/{groupId}/by-category")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_READER')")
    public List<CategoryStats> getGroupStatsByCategory(@PathVariable Long groupId) {
        return statsService.getGroupStatsByCategory(groupId);
    }
}
