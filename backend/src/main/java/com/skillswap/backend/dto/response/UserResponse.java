package com.skillswap.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String id;
    private String name;
    private String email;
    private String profileImageUrl;
    private String bio;
    private String location;
    private String experienceLevel;
    private boolean emailVerified;
    private Instant createdAt;
}