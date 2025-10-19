package com.example.family_budget_pet.exceptions;

public class GroupNotFoundException extends RuntimeException{

    public GroupNotFoundException() {
    }

    public GroupNotFoundException(String message) {
        super(message);
    }
}
