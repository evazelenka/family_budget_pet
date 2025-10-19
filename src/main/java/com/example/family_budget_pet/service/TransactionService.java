package com.example.family_budget_pet.service;

import com.example.family_budget_pet.domain.Category;
import com.example.family_budget_pet.domain.Transaction;
import com.example.family_budget_pet.domain.User;
import com.example.family_budget_pet.exceptions.CategoryNotFoundException;
import com.example.family_budget_pet.exceptions.TransactionNotFoundException;
import com.example.family_budget_pet.exceptions.UserNotFoundException;
import com.example.family_budget_pet.repository.CategoryRepository;
import com.example.family_budget_pet.repository.TransactionRepository;
import com.example.family_budget_pet.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Метод создания транзакции.
     * @param transaction транзакция
     * @param type тип категории транзакции
     * @param description описание транзакции
     * @param username имя пользователя, создавшего транзакцию
     * @return сохраненная транзакция
     */
    @Transactional
    public Transaction save(Transaction transaction, String type, String description, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username + " пользователь не найден"));
        Category c = categoryRepository.findByName(type).orElseThrow(() -> new CategoryNotFoundException(type + " категория не найдена"));

        transaction.setDate(LocalDateTime.now());
        transaction.setUser(user);
        transaction.setDescription(description);
        transaction.setCategory(c);
        return repository.save(transaction);
    }

    /**
     * Метод обновления данных транзакции.
     * @param t обновленная транзакция
     * @param type новый тип категории транзакции
     * @return сохраненная транзакция
     */
    @Transactional
    public Transaction update(Transaction t, String type){
        Transaction tran = findById(t.getId());
        Category c = categoryRepository.findByName(type).orElseThrow(() -> new CategoryNotFoundException(type + " категория не найдена"));

        t.setCategory(c);
        if (t.getAmount() != null){
            tran.setAmount(t.getAmount());
        }
        if (t.getDate() != null){
            tran.setDate(t.getDate());
        }
        if (t.getCategory() != null){
            tran.setCategory(t.getCategory());
        }
        if (t.getDescription() != null){
            tran.setDescription(t.getDescription());
        }
        return repository.save(tran);
    }

    /**
     * Метод удаления транзакции по id.
     * @param id id транзакции
     */
    @Transactional
    public void deleteById(Long id){
        repository.deleteById(id);
    }

    /**
     * Метод поиска транзакции по id.
     * @param id id транзакции
     * @return найденная транзакция
     */
    public Transaction findById(Long id){
        return repository.findById(id).orElseThrow(() -> new TransactionNotFoundException(id + " транзакция не найдена"));
    }

    /**
     * Метод обновления описания транзакции
     * @param id id транзакции
     * @param newDescription новое описание
     * @return обновленная транзакция
     * @throws TransactionNotFoundException исключение, которое может возникнуть при запросе транзакции из БД
     */
    @Transactional
    public Transaction updateTransDescription(Long id, String newDescription) throws TransactionNotFoundException{
        Transaction t = repository.findById(id).orElseThrow(() -> new TransactionNotFoundException(id + " транзакция не найдена"));
        t.setDescription(newDescription);
        return repository.save(t);
    }

    /**
     * Метод поиска всех транзакций одной группы.
     * @param groupId id группы
     * @return список транзакций группы
     */
    public List<Transaction> findAllByGroupId(Long groupId){
        return repository.findAllByGroupId(groupId);
    }
}

