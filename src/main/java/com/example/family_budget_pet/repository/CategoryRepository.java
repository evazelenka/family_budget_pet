package com.example.family_budget_pet.repository;

import com.example.family_budget_pet.domain.Category;
import com.example.family_budget_pet.domain.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByType(CategoryType type);

}
