package com.skillswap.backend.service;

import com.skillswap.backend.dto.request.ReviewRequest;
import com.skillswap.backend.dto.response.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    ReviewResponse createReview(String courseId, String userId, ReviewRequest request);
    ReviewResponse updateReview(String reviewId, String userId, ReviewRequest request);
    void deleteReview(String reviewId, String userId);
    Page<ReviewResponse> getCourseReviews(String courseId, Pageable pageable);
}