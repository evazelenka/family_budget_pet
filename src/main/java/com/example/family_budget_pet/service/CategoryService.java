package com.example.family_budget_pet.service;

import com.example.family_budget_pet.domain.Category;
import com.example.family_budget_pet.domain.enums.CategoryType;
import com.example.family_budget_pet.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public List<Category> findAllByType(CategoryType type){
        return repository.findAllByType(type);
    }

    public Category findByName(String name){
        return repository.findByName(name);
    }

    public Category save(Category category){
        return repository.save(category);
    }


    public void deleteById(Long id){
        repository.deleteById(id);
    }
}
