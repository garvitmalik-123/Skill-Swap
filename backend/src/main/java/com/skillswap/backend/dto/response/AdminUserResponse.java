package com.skillswap.backend.dto.response;

import com.skillswap.backend.entity.User.AccountStatus;
import com.skillswap.backend.entity.User.ExperienceLevel;
import lombok.*;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
public class AdminUserResponse {
    private String id;
    private String name;
    private String email;
    private Set<String> roles;
    private AccountStatus accountStatus;
    private ExperienceLevel experienceLevel;
    private boolean emailVerified;
    private Instant createdAt;
}