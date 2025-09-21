package com.example.family_budget_pet.repository;

import com.example.family_budget_pet.domain.Transaction;
import com.example.family_budget_pet.domain.dto.CategoryStats;
import com.example.family_budget_pet.domain.dto.TypeStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
//    List<Transaction> findByUser(User user);
//    List<Transaction> findByUserAndDateBetween(User user, LocalDateTime start, LocalDateTime end);
//    List<Transaction> findByUserAndCategoryId(User user, Long categoryId);
//
//    // сумма по пользователю и типу (income / expense)
//    @Query("SELECT COALESCE(SUM(t.amount), 0) " +
//            "FROM Transaction t " +
//            "WHERE t.user.id = :userId AND t.category.type = :type")
//    BigDecimal sumByUserAndType(@Param("userId") Long userId,
//                                @Param("type") String type);
//
//    // сгруппировано по типу сразу
//    @Query("SELECT t.category.type, COALESCE(SUM(t.amount), 0) " +
//            "FROM Transaction t " +
//            "WHERE t.user.id = :userId " +
//            "GROUP BY t.category.type")
//    List<Object[]> sumByUserGrouped(@Param("userId") Long userId);
//}

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
//    @Query("SELECT t.category.name AS categoryName, SUM(t.amount) AS total " +
//            "FROM Transaction t " +
//            "JOIN t.user u " +
//            "JOIN u.groups g " +
//            "WHERE g.id = :groupId " +
//            "GROUP BY t.category.name")
//    List<CategoryStats> findGroupStatsByCategory(Long groupId);

    @Query("SELECT t FROM Transaction t WHERE t.user.group.id = :groupId")
    List<Transaction> findAllByGroupId(@Param("groupId") Long groupId);
}
