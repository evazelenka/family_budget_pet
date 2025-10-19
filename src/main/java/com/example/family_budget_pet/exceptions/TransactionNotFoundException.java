package com.example.family_budget_pet.exceptions;

public class TransactionNotFoundException extends RuntimeException{

    public TransactionNotFoundException() {
    }

    public TransactionNotFoundException(String message) {
        super(message);
    }
}
