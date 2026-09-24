package com.skillswap.backend.repository;

import com.skillswap.backend.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review, String> {

    Page<Review> findByCourseIdAndStatus(String courseId, Review.ReviewStatus status, Pageable pageable);

    Optional<Review> findByUserIdAndCourseId(String userId, String courseId);

    boolean existsByUserIdAndCourseId(String userId, String courseId);

    long countByCourseIdAndStatus(String courseId, Review.ReviewStatus status);
}