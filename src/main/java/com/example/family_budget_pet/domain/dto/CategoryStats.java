package com.example.family_budget_pet.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CategoryStats {
    String getCategoryName();
    BigDecimal getTotal();
    LocalDateTime getDate();
}
