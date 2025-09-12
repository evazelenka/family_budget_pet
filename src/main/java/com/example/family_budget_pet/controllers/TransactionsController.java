package com.example.family_budget_pet.controllers;

import com.example.family_budget_pet.domain.Category;
import com.example.family_budget_pet.domain.Transaction;
import com.example.family_budget_pet.domain.User;
import com.example.family_budget_pet.domain.enums.CategoryType;
import com.example.family_budget_pet.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionsController {

    private final TransactionService service;

    @PostMapping("/add")
    public String addTrans(@ModelAttribute("transaction")Transaction transaction, @RequestParam String type, @RequestParam String description, Model model, @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal){
        service.save(transaction, type, description, principal.getUsername());

        model.addAttribute("title", "Профиль");
        return "general/3.html";
    }
}
