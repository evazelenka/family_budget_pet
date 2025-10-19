package com.example.family_budget_pet.exceptions;

public class CategoryNotFoundException extends RuntimeException{

    public CategoryNotFoundException() {
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
