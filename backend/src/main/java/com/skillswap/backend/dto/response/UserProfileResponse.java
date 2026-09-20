package com.skillswap.backend.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
@Builder
public class UserProfileResponse {
    private String id;
    private String email;
    private String name;
    private String bio;
    private String location;
    private String profileImageUrl;
    private String experienceLevel;
    private String accountStatus;
    private Instant createdAt;
    private List<UserSkillResponse> teachingSkills;
    private List<UserSkillResponse> learningSkills;
}