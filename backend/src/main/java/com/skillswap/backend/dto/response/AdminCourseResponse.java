package com.skillswap.backend.dto.response;

import com.skillswap.backend.entity.Course.CourseStatus;
import com.skillswap.backend.entity.Course.CourseType;
import lombok.*;

import java.time.Instant;

@Data
@Builder
public class AdminCourseResponse {
    private String id;
    private String title;
    private String creatorId;
    private String creatorName;
    private CourseType type;
    private CourseStatus status;
    private String rejectionReason;
    private double averageRating;
    private int enrollmentCount;
    private Instant createdAt;
}