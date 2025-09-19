package com.example.family_budget_pet;

import com.example.family_budget_pet.domain.Role;
import com.example.family_budget_pet.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            // Список ролей, которые нужны в системе
            String[] roles = {"ROLE_ADMIN", "ROLE_USER", "ROLE_READER"};

            for (String roleName : roles) {
                roleRepository.findByName(roleName)
                        .orElseGet(() -> {
                            Role newRole = new Role();
                            newRole.setName(roleName);
                            return roleRepository.save(newRole);
                        });
            }

            System.out.println("✅ Роли инициализированы");
        };
    }
}
