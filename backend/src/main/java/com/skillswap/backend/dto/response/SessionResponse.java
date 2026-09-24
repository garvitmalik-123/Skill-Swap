package com.skillswap.backend.dto.response;

import com.skillswap.backend.entity.SkillSession.SessionStatus;
import com.skillswap.backend.entity.SkillSession.SessionType;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class SessionResponse {
    private String id;
    private String teacherId;
    private String teacherName;
    private String skillId;
    private String skillName;
    private String title;
    private String description;
    private Integer durationMinutes;
    private SessionType type;
    private Double price;
    private Integer skillPointCost;
    private List<Instant> availableSlots;
    private SessionStatus status;
    private Instant createdAt;
}