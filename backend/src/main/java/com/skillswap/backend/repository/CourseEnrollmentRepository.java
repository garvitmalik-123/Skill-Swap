package com.skillswap.backend.repository;

import com.skillswap.backend.entity.CourseEnrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CourseEnrollmentRepository extends MongoRepository<CourseEnrollment, String> {

    Optional<CourseEnrollment> findByUserIdAndCourseId(String userId, String courseId);

    boolean existsByUserIdAndCourseId(String userId, String courseId);

    Page<CourseEnrollment> findByUserId(String userId, Pageable pageable);
}