package com.example.family_budget_pet.exceptions;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
