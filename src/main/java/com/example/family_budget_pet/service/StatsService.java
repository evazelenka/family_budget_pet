package com.example.family_budget_pet.service;

import com.example.family_budget_pet.domain.Category;
import com.example.family_budget_pet.domain.Transaction;
import com.example.family_budget_pet.domain.User;
import com.example.family_budget_pet.domain.dto.CategoryStats;
import com.example.family_budget_pet.domain.dto.TypeStats;
import com.example.family_budget_pet.repository.CategoryRepository;
import com.example.family_budget_pet.repository.TransactionRepository;
import com.example.family_budget_pet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class StatsService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public List<CategoryStats> getCategoryStats(
            String categoryName,
            String username,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();

        Root<Transaction> root = query.from(Transaction.class);

        List<Predicate> predicates = new ArrayList<>();

        Category category = categoryRepository.findByName(categoryName);
        categoryName = category != null ? categoryName : null;

        User user = userRepository.findByUsername(username).orElse(null);
        username = user != null ? username : null;

        if (categoryName != null) {
            predicates.add(cb.equal(root.get("category").get("name"), categoryName));
        }
        if (username != null) {
            predicates.add(cb.equal(root.get("user").get("username"), username));
        }
        if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("date"), startDate));
        }
        if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("date"), endDate));
        }

        // группировка по категории и дате
        query.multiselect(
                        root.get("category").get("name").alias("categoryName"),
                        cb.sum(root.get("amount")).alias("total"),
                        root.get("date").alias("date")
                )
                .where(cb.and(predicates.toArray(new Predicate[0])))
                .groupBy(root.get("category").get("name"), root.get("date"));

        List<Tuple> tuples = entityManager.createQuery(query).getResultList();

        List<CategoryStats> result = new ArrayList<>();
        for (Tuple t : tuples) {
            result.add(new CategoryStats() {
                @Override
                public String getCategoryName() {
                    return t.get("categoryName", String.class);
                }

                @Override
                public BigDecimal getTotal() {
                    return t.get("total", BigDecimal.class);
                }

                @Override
                public LocalDateTime getDate() {
                    return t.get("date", LocalDateTime.class);
                }
            });
        }

        return result;
    }

    public List<TypeStats> getUserStatsByType(Long userId) {
        return transactionRepository.findUserStatsByType(userId);
    }

    public List<CategoryStats> getUserStatsByCategory(Long userId) {
        return transactionRepository.findUserStatsByCategory(userId);
    }

//    public List<TypeStats> getGroupStatsByType(Long groupId) {
//        return transactionRepository.findGroupStatsByType(groupId);
//    }
//
    public List<CategoryStats> getGroupStatsByCategory(Long groupId) {
        return transactionRepository.findGroupStatsByCategory(groupId);
    }

    public List<CategoryStats> getFilterStats(String username, String categoryName, LocalDateTime dateStart, LocalDateTime dateEnd) {
        Category c = categoryRepository.findByName(categoryName);
        if (c != null){
            return transactionRepository.findFilteredStats(username, c.getId(), dateStart, dateEnd);
        }
        return transactionRepository.findFilteredStats(username, null, dateStart, dateEnd);
    }
}
