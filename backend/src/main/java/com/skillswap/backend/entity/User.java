package com.skillswap.backend.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    private String passwordHash;

    private String name;

    private Set<Role> roles;

    private boolean emailVerified;

    private String bio;
    private String location;
    private String profileImageUrl;
    private ExperienceLevel experienceLevel;
    private AccountStatus accountStatus;

    private Instant createdAt;
    private Instant updatedAt;

    public enum Role {
        USER, ADMIN
    }

    public enum ExperienceLevel {
        BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
    }

    public enum AccountStatus {
        ACTIVE, SUSPENDED, DEACTIVATED
    }
}