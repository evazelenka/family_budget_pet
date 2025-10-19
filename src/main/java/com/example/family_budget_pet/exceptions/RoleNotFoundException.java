package com.example.family_budget_pet.exceptions;

public class RoleNotFoundException extends RuntimeException{

    public RoleNotFoundException() {
    }

    public RoleNotFoundException(String message) {
        super(message);
    }
}
