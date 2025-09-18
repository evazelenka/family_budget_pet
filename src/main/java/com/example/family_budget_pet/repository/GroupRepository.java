package com.example.family_budget_pet.repository;

import com.example.family_budget_pet.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByToken(String token);
    Optional<Group> findByAdmin_Id(Long adminId);
    @Query("SELECT g FROM Group g JOIN g.users u WHERE u.id = :userId")
    Optional<Group> findByUserId(@Param("userId") Long userId);
}
