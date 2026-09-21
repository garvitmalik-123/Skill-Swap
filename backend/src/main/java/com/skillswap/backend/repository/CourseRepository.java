package com.skillswap.backend.repository;

import com.skillswap.backend.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseRepository extends MongoRepository<Course, String>, CourseRepositoryCustom {

    Page<Course> findByStatus(Course.CourseStatus status, Pageable pageable);

    Page<Course> findByCreatorId(String creatorId, Pageable pageable);

    Page<Course> findByCategoryAndStatus(String category, Course.CourseStatus status, Pageable pageable);
}