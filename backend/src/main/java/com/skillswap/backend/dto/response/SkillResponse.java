package com.skillswap.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkillResponse {
    private String id;
    private String name;
    private String categoryId;
    private String description;
}