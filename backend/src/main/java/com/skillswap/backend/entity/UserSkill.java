package com.skillswap.backend.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_skills")
@CompoundIndex(name = "user_skill_type_idx", def = "{'userId': 1, 'skillId': 1, 'relationType': 1}", unique = true)
public class UserSkill {

    @Id
    private String id;

    private String userId;

    private String skillId;

    private RelationType relationType;

    private SkillLevel level;

    private Instant createdAt;

    public enum RelationType {
        CAN_TEACH, WANTS_TO_LEARN
    }

    public enum SkillLevel {
        BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
    }
}