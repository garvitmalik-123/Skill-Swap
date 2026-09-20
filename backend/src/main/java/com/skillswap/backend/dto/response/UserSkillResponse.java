package com.skillswap.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSkillResponse {
    private String id;
    private String skillId;
    private String skillName;
    private String relationType;
    private String level;
}