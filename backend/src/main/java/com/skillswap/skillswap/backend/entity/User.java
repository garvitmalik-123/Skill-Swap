package com.skillswap.skillswap.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String email;

    /** BCrypt hash — never store or return the raw password. */
    private String passwordHash;

    private String profileImageUrl;
    private String bio;
    private String location;
    private String experienceLevel;

    @Builder.Default
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    @Builder.Default
    private Set<Role> roles = new HashSet<>(Set.of(Role.USER));

    @Builder.Default
    private boolean emailVerified = false;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public enum AccountStatus {
        ACTIVE, SUSPENDED, DEACTIVATED
    }

    public enum Role {
        USER, ADMIN
    }
}