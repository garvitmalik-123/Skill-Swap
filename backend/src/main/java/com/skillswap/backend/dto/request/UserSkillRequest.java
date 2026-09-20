package com.skillswap.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserSkillRequest {

    @NotBlank(message = "Skill ID is required")
    private String skillId;

    @NotNull(message = "Relation type is required")
    private String relationType;

    private String level;
}