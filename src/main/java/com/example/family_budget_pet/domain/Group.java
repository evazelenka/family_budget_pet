package com.example.family_budget_pet.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupName;

    @OneToOne
    @JoinColumn(name = "admin_id")
    private User admin;

    @OneToMany(mappedBy = "group")
    private Set<User> users = new HashSet<>();

    private String token;

    public void addUser(User user){
        users.add(user);
    }
}
