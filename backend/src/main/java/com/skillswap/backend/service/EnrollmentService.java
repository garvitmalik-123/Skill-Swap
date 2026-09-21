package com.skillswap.backend.service;

import com.skillswap.backend.dto.response.EnrollmentResponse;
import com.skillswap.backend.dto.response.ProgressResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EnrollmentService {

    EnrollmentResponse enroll(String userId, String courseId);

    Page<EnrollmentResponse> getMyEnrollments(String userId, Pageable pageable);

    EnrollmentResponse getEnrollment(String userId, String courseId);

    void markLessonComplete(String userId, String courseId, String lessonId);

    ProgressResponse getProgress(String userId, String courseId);
}