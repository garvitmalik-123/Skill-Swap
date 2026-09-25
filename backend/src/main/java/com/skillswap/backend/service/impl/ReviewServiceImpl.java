package com.skillswap.backend.service.impl;

import com.skillswap.backend.dto.request.ReviewRequest;
import com.skillswap.backend.dto.response.ReviewResponse;
import com.skillswap.backend.entity.Course;
import com.skillswap.backend.entity.Notification.NotificationType;
import com.skillswap.backend.entity.Review;
import com.skillswap.backend.exception.*;
import com.skillswap.backend.repository.*;
import com.skillswap.backend.service.NotificationService;
import com.skillswap.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final CourseEnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    public ReviewResponse createReview(String courseId, String userId, ReviewRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        boolean enrolled = enrollmentRepository.existsByUserIdAndCourseId(userId, courseId);
        if (!enrolled) {
            throw new ForbiddenException("Only enrolled users can review this course");
        }

        if (reviewRepository.existsByUserIdAndCourseId(userId, courseId)) {
            throw new DuplicateResourceException("You have already reviewed this course");
        }

        Review review = Review.builder()
                .courseId(courseId)
                .userId(userId)
                .rating(request.getRating())
                .comment(request.getComment())
                .status(Review.ReviewStatus.ACTIVE)
                .build();

        review = reviewRepository.save(review);
        recalculateRating(courseId);

        notificationService.notify(
                course.getCreatorId(),
                NotificationType.NEW_REVIEW,
                "New review on your course",
                "Your course \"" + course.getTitle() + "\" received a " + request.getRating() + "-star review",
                courseId,
                "COURSE");

        return mapToResponse(review);
    }

    @Override
    public ReviewResponse updateReview(String reviewId, String userId, ReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getUserId().equals(userId)) {
            throw new ForbiddenException("You can only edit your own review");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review = reviewRepository.save(review);

        recalculateRating(review.getCourseId());
        return mapToResponse(review);
    }

    @Override
    public void deleteReview(String reviewId, String userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getUserId().equals(userId)) {
            throw new ForbiddenException("You can only delete your own review");
        }

        reviewRepository.delete(review);
        recalculateRating(review.getCourseId());
    }

    @Override
    public Page<ReviewResponse> getCourseReviews(String courseId, Pageable pageable) {
        return reviewRepository
                .findByCourseIdAndStatus(courseId, Review.ReviewStatus.ACTIVE, pageable)
                .map(this::mapToResponse);
    }

    private void recalculateRating(String courseId) {
        long count = reviewRepository.countByCourseIdAndStatus(courseId, Review.ReviewStatus.ACTIVE);
        double avg = reviewRepository
                .findByCourseIdAndStatus(courseId, Review.ReviewStatus.ACTIVE, org.springframework.data.domain.Pageable.unpaged())
                .stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        Course course = courseRepository.findById(courseId).orElseThrow();
        course.setAverageRating(avg);
        course.setReviewCount((int) count);
        courseRepository.save(course);
    }

    private ReviewResponse mapToResponse(Review review) {
        String userName = userRepository.findById(review.getUserId())
                .map(u -> u.getName())
                .orElse("Unknown");

        return ReviewResponse.builder()
                .id(review.getId())
                .courseId(review.getCourseId())
                .userId(review.getUserId())
                .userName(userName)
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}