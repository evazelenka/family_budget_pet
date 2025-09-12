package com.example.family_budget_pet.domain.dto;

import java.math.BigDecimal;

public interface CategoryStats {
    String getCategoryName();
    BigDecimal getTotal();
}
