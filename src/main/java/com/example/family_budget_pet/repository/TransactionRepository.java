package com.example.family_budget_pet.repository;

import com.example.family_budget_pet.domain.Transaction;
import com.example.family_budget_pet.domain.dto.CategoryStats;
import com.example.family_budget_pet.domain.dto.TypeStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

//     ====== Пользователь ======
    @Query("SELECT t.category.type AS type, SUM(t.amount) AS total " +
            "FROM Transaction t " +
            "WHERE t.user.id = :userId " +
            "GROUP BY t.category.type")
    List<TypeStats> findUserStatsByType(Long userId);

    @Query("SELECT t.category.name AS categoryName, SUM(t.amount) AS total " +
            "FROM Transaction t " +
            "WHERE t.user.id = :userId " +
            "GROUP BY t.category.name")
    List<CategoryStats> findUserStatsByCategory(Long userId);


////     ====== Админ (по группе) ======
//    @Query("SELECT t.category.type AS type, SUM(t.amount) AS total " +
//            "FROM Transaction t " +
//            "JOIN t.user u " +
//            "JOIN u.groups g " +
//            "WHERE g.id = :groupId " +
//            "GROUP BY t.category.type")
//    List<TypeStats> findGroupStatsByType(Long groupId);
//
    @Query("SELECT t.category.name AS categoryName, SUM(t.amount) AS total " +
            "FROM Transaction t " +
            "JOIN t.user u " +
            "JOIN u.group g " +
            "WHERE g.id = :groupId " +
            "GROUP BY t.category.name")
    List<CategoryStats> findGroupStatsByCategory(Long groupId);

    @Query("SELECT t FROM Transaction t WHERE t.user.group.id = :groupId")
    List<Transaction> findAllByGroupId(@Param("groupId") Long groupId);
}
