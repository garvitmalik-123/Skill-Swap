package com.skillswap.backend.repository;

import com.skillswap.backend.entity.Lesson;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LessonRepository extends MongoRepository<Lesson, String> {

    List<Lesson> findByCourseIdOrderByOrderAsc(String courseId);

    List<Lesson> findByCourseIdAndPublishedTrueOrderByOrderAsc(String courseId);

    long countByCourseId(String courseId);

    void deleteByCourseId(String courseId);
}