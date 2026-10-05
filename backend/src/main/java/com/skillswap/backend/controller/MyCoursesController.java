package com.skillswap.backend.controller;

import com.skillswap.backend.dto.response.ApiResponse;
import com.skillswap.backend.dto.response.CourseResponse;
import com.skillswap.backend.security.CustomUserDetails;
import com.skillswap.backend.service.CourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/me/courses")
@RequiredArgsConstructor
@Tag(name = "Courses", description = "Courses created by the logged-in user")
public class MyCoursesController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getMyCourses(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(
                ApiResponse.success(courseService.getCoursesByCreator(currentUser.getUserId())));
    }
}