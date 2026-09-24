package com.skillswap.backend.controller;

import com.skillswap.backend.dto.request.ReviewRequest;
import com.skillswap.backend.dto.response.ReviewResponse;
import com.skillswap.backend.security.CustomUserDetails;
import com.skillswap.backend.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/api/courses/{id}/reviews")
    public ResponseEntity<ReviewResponse> createReview(
            @PathVariable String id,
            @Valid @RequestBody ReviewRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String userId = userDetails.getUserId(); // adjust to whatever getter CustomUserDetails actually exposes
        return ResponseEntity.ok(reviewService.createReview(id, userId, request));
    }

    @GetMapping("/api/courses/{id}/reviews")
    public ResponseEntity<Page<ReviewResponse>> getReviews(
            @PathVariable String id,
            Pageable pageable) {
        return ResponseEntity.ok(reviewService.getCourseReviews(id, pageable));
    }

    @PutMapping("/api/reviews/{id}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable String id,
            @Valid @RequestBody ReviewRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String userId = userDetails.getUserId();
        return ResponseEntity.ok(reviewService.updateReview(id, userId, request));
    }

    @DeleteMapping("/api/reviews/{id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable String id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String userId = userDetails.getUserId();
        reviewService.deleteReview(id, userId);
        return ResponseEntity.noContent().build();
    }
}