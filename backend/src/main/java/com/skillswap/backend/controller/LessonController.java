package com.skillswap.backend.controller;

import com.skillswap.backend.dto.request.LessonRequest;
import com.skillswap.backend.dto.response.LessonResponse;
import com.skillswap.backend.security.CustomUserDetails;
import com.skillswap.backend.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses/{courseId}/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @GetMapping
    public ResponseEntity<List<LessonResponse>> getLessons(
            @PathVariable String courseId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        String requesterId = currentUser != null ? currentUser.getUserId() : null;
        return ResponseEntity.ok(lessonService.getLessonsForCourse(courseId, requesterId));
    }

    @PostMapping
    public ResponseEntity<LessonResponse> createLesson(
            @PathVariable String courseId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody LessonRequest request) {
        return ResponseEntity.ok(lessonService.createLesson(courseId, currentUser.getUserId(), request));
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<LessonResponse> updateLesson(
            @PathVariable String courseId,
            @PathVariable String lessonId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody LessonRequest request) {
        return ResponseEntity.ok(lessonService.updateLesson(courseId, lessonId, currentUser.getUserId(), request));
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Void> deleteLesson(
            @PathVariable String courseId,
            @PathVariable String lessonId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        lessonService.deleteLesson(courseId, lessonId, currentUser.getUserId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lessonId}/publish")
    public ResponseEntity<LessonResponse> publishLesson(
            @PathVariable String courseId,
            @PathVariable String lessonId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(lessonService.publishLesson(courseId, lessonId, currentUser.getUserId()));
    }

    @PutMapping("/{lessonId}/reorder")
    public ResponseEntity<LessonResponse> reorderLesson(
            @PathVariable String courseId,
            @PathVariable String lessonId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(lessonService.reorderLesson(courseId, lessonId, currentUser.getUserId(), body.get("order")));
    }
}