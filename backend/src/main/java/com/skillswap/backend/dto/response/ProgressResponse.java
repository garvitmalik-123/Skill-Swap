package com.skillswap.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProgressResponse {
    private String courseId;
    private int totalLessons;
    private int completedLessons;
    private double progressPercentage;
    private boolean courseCompleted;
}