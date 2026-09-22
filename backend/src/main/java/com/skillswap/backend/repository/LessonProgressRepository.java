package com.skillswap.backend.repository;

import com.skillswap.backend.entity.LessonProgress;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LessonProgressRepository extends MongoRepository<LessonProgress, String> {

    Optional<LessonProgress> findByUserIdAndLessonId(String userId, String lessonId);

    List<LessonProgress> findByUserIdAndCourseId(String userId, String courseId);

    long countByUserIdAndCourseIdAndCompletedTrue(String userId, String courseId);
}