package com.example.family_budget_pet.controllers;

import com.example.family_budget_pet.domain.Group;
import com.example.family_budget_pet.domain.Transaction;
import com.example.family_budget_pet.domain.User;
import com.example.family_budget_pet.service.CategoryService;
import com.example.family_budget_pet.service.GroupService;
import com.example.family_budget_pet.service.TransactionService;
import com.example.family_budget_pet.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionsController {

    private final TransactionService service;
    private final UserService userService;
    private final CategoryService categoryService;
    private final GroupService groupService;

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
            return "redirect:/transactions/my";
        }
        service.deleteById(id);
        return "redirect:/transactions/my";
    }

    @GetMapping("/update/{id}")
    public String updateTran(@PathVariable Long id, Model model){
        Transaction t = service.findById(id);
        if (t == null){
            model.addAttribute("error", "Транзакция не найдена");
            return "redirect:/transactions/my";
        }
        model.addAttribute("tran", t);
        model.addAttribute("transaction", t);
        return "general/update-transaction";
    }

    @PostMapping("/update")
    public String updateTran(@ModelAttribute("transaction") Transaction t, @RequestParam String type){
        service.update(t, type);
        return "redirect:/transactions/my";
    }

    @PostMapping("/delete-user-tran")
    public String deleteUserTran(@RequestParam Long id2, Model model){
        Transaction t = service.findById(id2);
        if (t == null){
            model.addAttribute("error", "Транзакция не найдена");
            return "redirect:/transactions/group";
        }
        service.deleteById(id2);
        return "redirect:/transactions/group";
    }

    @GetMapping("/my")
    public String showMyTransactions(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model){
//        Category home = Category.builder().name("Дом").type(CategoryType.EXPENSE).build();
//        Category health = Category.builder().name("Здоровье").type(CategoryType.EXPENSE).build();
//        Category food = Category.builder().name("Еда").type(CategoryType.EXPENSE).build();
//        Category salary = Category.builder().name("Зарплата").type(CategoryType.INCOME).build();
//        Category debt_back = Category.builder().name("Возврат долгов").type(CategoryType.INCOME).build();
//        Category passive = Category.builder().name("Пассивный доход").type(CategoryType.INCOME).build();
//       categoryService.deleteById(7L);
//       categoryService.deleteById(8L);
//       categoryService.deleteById(9L);
//       categoryService.deleteById(10L);
//       categoryService.deleteById(11L);
//       categoryService.deleteById(12L);


        User user = userService.findByUsername(principal.getUsername());
        List<Transaction> transactions = user.getTransactions();
        if (transactions != null){
            model.addAttribute("trans", transactions);
        }
        model.addAttribute("title", "Транзакции");
        model.addAttribute("transaction", new Transaction());
        return "general/trans";
    }

    @GetMapping("/group")
    public String showGroupTransactions(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model){
        User user = userService.findByUsername(principal.getUsername());
        Group group = groupService.findByUserId(user.getId()).orElse(null);
        if (group != null){
            List<Transaction> transactions = service.findAllByGroupId(group.getId());
            model.addAttribute("trans", transactions);
        }
        model.addAttribute("title", "Транзакции");
        return "admin-reader/users-trans";
    }

    @PostMapping("/update-desc")
    public String updateTransDescription(@RequestParam Long id, @RequestParam String description, Model model){
        service.updateTransDescription(id, description);
        return "redirect:/transactions/group";
    }
}
