package com.skillswap.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "course_enrollments")
@CompoundIndex(name = "user_course_idx", def = "{'userId': 1, 'courseId': 1}", unique = true)
public class CourseEnrollment {

    @Id
    private String id;

    private String userId;

    private String courseId;

    private Course.CourseType enrollmentType;

    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

    @CreatedDate
    private Instant enrolledAt;

    private Instant completedAt;

    public enum EnrollmentStatus {
        ACTIVE, COMPLETED, CANCELLED
    }
}