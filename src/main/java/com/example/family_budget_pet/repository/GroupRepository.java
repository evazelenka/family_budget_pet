package com.example.family_budget_pet.repository;

import com.example.family_budget_pet.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
