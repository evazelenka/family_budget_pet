package com.example.family_budget_pet.repository.spec;

import com.example.family_budget_pet.domain.Transaction;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class TransactionSpecification {

    public static Specification<Transaction> filter(
            Long categoryId,
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (categoryId != null) {
                predicates.add((Predicate) cb.equal(root.get("category").get("id"), categoryId));
            }
            if (userId != null) {
                predicates.add((Predicate) cb.equal(root.get("user").get("id"), userId));
            }
            if (startDate != null) {
                predicates.add((Predicate) cb.greaterThanOrEqualTo(root.get("date"), startDate));
            }
            if (endDate != null) {
                predicates.add((Predicate) cb.lessThanOrEqualTo(root.get("date"), endDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
