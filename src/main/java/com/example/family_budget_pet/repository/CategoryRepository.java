package com.example.family_budget_pet.repository;

import com.example.family_budget_pet.domain.Category;
import com.example.family_budget_pet.domain.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByType(CategoryType type);
    Optional<Category> findByName(String name);
}
