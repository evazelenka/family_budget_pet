package com.example.family_budget_pet.repository;

import com.example.family_budget_pet.domain.Category;
import com.example.family_budget_pet.domain.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByType(CategoryType type);
}
