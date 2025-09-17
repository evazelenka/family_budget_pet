package com.example.family_budget_pet.controllers;

import com.example.family_budget_pet.domain.Category;
import com.example.family_budget_pet.domain.Transaction;
import com.example.family_budget_pet.domain.User;
import com.example.family_budget_pet.domain.enums.CategoryType;
import com.example.family_budget_pet.service.CategoryService;
import com.example.family_budget_pet.service.TransactionService;
import com.example.family_budget_pet.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionsController {

    private final TransactionService service;
    private final UserService userService;
    private final CategoryService categoryService;

    @PostMapping("/add")
    public String addTran(@ModelAttribute("transaction")Transaction transaction, @RequestParam String type, @RequestParam String description, Model model, @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal){
        User user = userService.findByUsername(principal.getUsername());
        Transaction transaction1 = service.save(transaction, type, description, user.getUsername());
        model.addAttribute("title", "Транзакции");
        model.addAttribute("trans", user.getTransactions());
        return "redirect:/transactions/my";
    }

    @PostMapping("/delete/{id}")
    public String deleteTran(@PathVariable Long id, Model model){
        Transaction t = service.findById(id);
        if (t == null){
            model.addAttribute("error", "Транзакция не найдена");
            return "reader/users";
        }
        model.addAttribute("title", "Транзакции");
        return "admin/trans";
    }

    @GetMapping("/my")
    public String showTransactions(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model){
//        Category home = Category.builder().name("Дом").type(CategoryType.EXPENSE).build();
//        Category health = Category.builder().name("Здоровье").type(CategoryType.EXPENSE).build();
//        Category food = Category.builder().name("Еда").type(CategoryType.EXPENSE).build();
//        Category salary = Category.builder().name("Зарплата").type(CategoryType.INCOME).build();
//        Category debt_back = Category.builder().name("Возврат долгов").type(CategoryType.INCOME).build();
//        Category passive = Category.builder().name("Пассивный доход").type(CategoryType.INCOME).build();
//       categoryService.save(home);
//       categoryService.save(health);
//       categoryService.save(food);
//       categoryService.save(salary);
//       categoryService.save(debt_back);
//       categoryService.save(passive);

        User user = userService.findByUsername(principal.getUsername());
        List<Transaction> transactions = user.getTransactions();
        if (transactions != null){
            model.addAttribute("trans", transactions);
        }
        model.addAttribute("title", "Транзакции");
        model.addAttribute("transaction", new Transaction());
        return "general/trans";
    }
}
