package com.skillswap.backend.entity;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "skill_sessions")
public class SkillSession {

    @Id
    private String id;

    @Indexed
    private String teacherId;

    @Indexed
    private String skillId;

    private String title;
    private String description;

    private Integer durationMinutes;

    @Builder.Default
    private SessionType type = SessionType.FREE;

    private Double price;
    private Integer skillPointCost;

    // Specific date-times the teacher is offering, not yet booked
    private List<Instant> availableSlots;

    @Builder.Default
    private SessionStatus status = SessionStatus.ACTIVE;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public enum SessionType {
        FREE, SKILLPOINT, PAID
    }

    public enum SessionStatus {
        ACTIVE, INACTIVE
    }
}