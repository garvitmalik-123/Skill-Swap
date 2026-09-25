package com.skillswap.backend.controller;

import com.skillswap.backend.dto.request.CourseRejectionRequest;
import com.skillswap.backend.dto.response.*;
import com.skillswap.backend.security.CustomUserDetails;
import com.skillswap.backend.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<Page<AdminUserResponse>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllUsers(pageable));
    }

    @PutMapping("/users/{id}/suspend")
    public ResponseEntity<Void> suspendUser(
            @PathVariable String id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        adminService.suspendUser(id, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{id}/reactivate")
    public ResponseEntity<Void> reactivateUser(
            @PathVariable String id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        adminService.reactivateUser(id, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/courses")
    public ResponseEntity<Page<AdminCourseResponse>> getAllCourses(Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllCourses(pageable));
    }

    @PutMapping("/courses/{id}/approve")
    public ResponseEntity<Void> approveCourse(
            @PathVariable String id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        adminService.approveCourse(id, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/courses/{id}/reject")
    public ResponseEntity<Void> rejectCourse(
            @PathVariable String id,
            @Valid @RequestBody CourseRejectionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        adminService.rejectCourse(id, userDetails.getUserId(), request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/transactions")
    public ResponseEntity<Page<AdminTransactionResponse>> getTransactions(Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllTransactions(pageable));
    }

    @GetMapping("/analytics")
    public ResponseEntity<AnalyticsResponse> getAnalytics() {
        return ResponseEntity.ok(adminService.getAnalytics());
    }
}