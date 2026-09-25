package com.skillswap.backend.service;

import com.skillswap.backend.dto.request.CourseRejectionRequest;
import com.skillswap.backend.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    Page<AdminUserResponse> getAllUsers(Pageable pageable);
    void suspendUser(String userId, String adminId);
    void reactivateUser(String userId, String adminId);

    Page<AdminTransactionResponse> getAllTransactions(Pageable pageable);

    AnalyticsResponse getAnalytics();

    Page<AdminCourseResponse> getAllCourses(Pageable pageable);
    void approveCourse(String courseId, String adminId);
    void rejectCourse(String courseId, String adminId, CourseRejectionRequest request);
}