package com.skillswap.backend.controller;

import com.skillswap.backend.dto.request.CreateCourseRequest;
import com.skillswap.backend.dto.request.UpdateCourseRequest;
import com.skillswap.backend.dto.response.ApiResponse;
import com.skillswap.backend.dto.response.CourseResponse;
import com.skillswap.backend.security.CustomUserDetails;
import com.skillswap.backend.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.skillswap.backend.dto.request.CourseSearchRequest;
import com.skillswap.backend.entity.Course;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody CreateCourseRequest request) {
        CourseResponse response = courseService.createCourse(currentUser.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Course created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CourseResponse>>> getAllCourses(Pageable pageable) {
        Page<CourseResponse> courses = courseService.getAllPublishedCourses(pageable);
        return ResponseEntity.ok(ApiResponse.success(courses));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<CourseResponse>>> searchCourses(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) String creatorId,
            @RequestParam(required = false) Course.CourseType type,
            @RequestParam(required = false) Course.Difficulty difficulty,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Integer minDuration,
            @RequestParam(required = false) Integer maxDuration,
            Pageable pageable) {

        CourseSearchRequest filters = new CourseSearchRequest();
        filters.setKeyword(keyword);
        filters.setCategory(category);
        filters.setSkill(skill);
        filters.setCreatorId(creatorId);
        filters.setType(type);
        filters.setDifficulty(difficulty);
        filters.setLanguage(language);
        filters.setMinPrice(minPrice);
        filters.setMaxPrice(maxPrice);
        filters.setMinRating(minRating);
        filters.setMinDuration(minDuration);
        filters.setMaxDuration(maxDuration);

        Page<CourseResponse> results = courseService.searchCourses(filters, pageable);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> getCourseById(@PathVariable String id) {
        CourseResponse response = courseService.getCourseById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> updateCourse(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String id,
            @Valid @RequestBody UpdateCourseRequest request) {
        CourseResponse response = courseService.updateCourse(id, currentUser.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success("Course updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String id) {
        courseService.deleteCourse(id, currentUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success("Course deleted successfully", null));
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<ApiResponse<CourseResponse>> publishCourse(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String id) {
        CourseResponse response = courseService.publishCourse(id, currentUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success("Course published successfully", response));
    }

    @PostMapping("/{id}/archive")
    public ResponseEntity<ApiResponse<CourseResponse>> archiveCourse(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String id) {
        CourseResponse response = courseService.archiveCourse(id, currentUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success("Course archived successfully", response));
    }
}