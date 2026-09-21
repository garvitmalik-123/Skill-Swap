package com.skillswap.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class LessonResponse {
    private String id;
    private String courseId;
    private String title;
    private String content;
    private Integer order;
    private String videoUrl;
    private String resourceUrl;
    private boolean published;
    private Integer durationMinutes;
    private Instant createdAt;
    private Instant updatedAt;
}