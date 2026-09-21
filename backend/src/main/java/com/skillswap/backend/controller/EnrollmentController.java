package com.skillswap.backend.controller;

import com.skillswap.backend.dto.response.ApiResponse;
import com.skillswap.backend.dto.response.EnrollmentResponse;
import com.skillswap.backend.dto.response.ProgressResponse;
import com.skillswap.backend.security.CustomUserDetails;
import com.skillswap.backend.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/api/courses/{courseId}/enroll")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> enroll(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String courseId) {
        EnrollmentResponse response = enrollmentService.enroll(currentUser.getUserId(), courseId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Enrolled successfully", response));
    }

    @GetMapping("/api/enrollments/me")
    public ResponseEntity<ApiResponse<Page<EnrollmentResponse>>> getMyEnrollments(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Pageable pageable) {
        Page<EnrollmentResponse> enrollments = enrollmentService.getMyEnrollments(currentUser.getUserId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(enrollments));
    }

    @GetMapping("/api/courses/{courseId}/enrollment")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> getEnrollment(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String courseId) {
        EnrollmentResponse response = enrollmentService.getEnrollment(currentUser.getUserId(), courseId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/api/courses/{courseId}/lessons/{lessonId}/complete")
    public ResponseEntity<ApiResponse<Void>> markLessonComplete(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String courseId,
            @PathVariable String lessonId) {
        enrollmentService.markLessonComplete(currentUser.getUserId(), courseId, lessonId);
        return ResponseEntity.ok(ApiResponse.success("Lesson marked as complete", null));
    }

    @GetMapping("/api/courses/{courseId}/progress")
    public ResponseEntity<ApiResponse<ProgressResponse>> getProgress(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String courseId) {
        ProgressResponse response = enrollmentService.getProgress(currentUser.getUserId(), courseId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}