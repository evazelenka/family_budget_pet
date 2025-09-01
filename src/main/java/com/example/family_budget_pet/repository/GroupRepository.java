package com.example.family_budget_pet.repository;

import com.example.family_budget_pet.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByToken(String token);
    Optional<Group> findByAdmin_Id(Long adminId);
}
