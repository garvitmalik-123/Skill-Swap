package com.skillswap.backend.dto.response;

import com.skillswap.backend.entity.Course;
import com.skillswap.backend.entity.CourseEnrollment;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class EnrollmentResponse {
    private String id;
    private String courseId;
    private String courseTitle;
    private Course.CourseType enrollmentType;
    private CourseEnrollment.EnrollmentStatus status;
    private Instant enrolledAt;
    private Instant completedAt;
}