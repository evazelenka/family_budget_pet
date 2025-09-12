package com.example.family_budget_pet.domain.dto;

import com.example.family_budget_pet.domain.enums.CategoryType;

import java.math.BigDecimal;

public interface TypeStats {
    CategoryType getType();
    BigDecimal getTotal();
}

