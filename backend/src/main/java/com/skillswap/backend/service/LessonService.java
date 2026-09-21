package com.skillswap.backend.service;

import com.skillswap.backend.dto.request.LessonRequest;
import com.skillswap.backend.dto.response.LessonResponse;
import com.skillswap.backend.entity.Course;
import com.skillswap.backend.entity.Lesson;
import com.skillswap.backend.exception.ForbiddenException;
import com.skillswap.backend.exception.ResourceNotFoundException;
import com.skillswap.backend.repository.CourseRepository;
import com.skillswap.backend.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    public List<LessonResponse> getLessonsForCourse(String courseId, String requesterId) {
        Course course = getCourseOrThrow(courseId);

        boolean isOwner = course.getCreatorId().equals(requesterId);

        List<Lesson> lessons = isOwner
                ? lessonRepository.findByCourseIdOrderByOrderAsc(courseId)
                : lessonRepository.findByCourseIdAndPublishedTrueOrderByOrderAsc(courseId);

        return lessons.stream().map(this::toResponse).toList();
    }

    public LessonResponse createLesson(String courseId, String requesterId, LessonRequest request) {
        Course course = getCourseOrThrow(courseId);
        assertOwner(course, requesterId);

        Lesson lesson = Lesson.builder()
                .courseId(courseId)
                .title(request.getTitle())
                .content(request.getContent())
                .order(request.getOrder())
                .videoUrl(request.getVideoUrl())
                .resourceUrl(request.getResourceUrl())
                .durationMinutes(request.getDurationMinutes())
                .published(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        lessonRepository.save(lesson);
        return toResponse(lesson);
    }

    public LessonResponse updateLesson(String courseId, String lessonId, String requesterId, LessonRequest request) {
        Course course = getCourseOrThrow(courseId);
        assertOwner(course, requesterId);

        Lesson lesson = getLessonOrThrow(lessonId, courseId);

        lesson.setTitle(request.getTitle());
        lesson.setContent(request.getContent());
        lesson.setOrder(request.getOrder());
        lesson.setVideoUrl(request.getVideoUrl());
        lesson.setResourceUrl(request.getResourceUrl());
        lesson.setDurationMinutes(request.getDurationMinutes());
        lesson.setUpdatedAt(Instant.now());

        lessonRepository.save(lesson);
        return toResponse(lesson);
    }

    public void deleteLesson(String courseId, String lessonId, String requesterId) {
        Course course = getCourseOrThrow(courseId);
        assertOwner(course, requesterId);

        Lesson lesson = getLessonOrThrow(lessonId, courseId);
        lessonRepository.delete(lesson);
    }

    public LessonResponse publishLesson(String courseId, String lessonId, String requesterId) {
        Course course = getCourseOrThrow(courseId);
        assertOwner(course, requesterId);

        Lesson lesson = getLessonOrThrow(lessonId, courseId);
        lesson.setPublished(true);
        lesson.setUpdatedAt(Instant.now());

        lessonRepository.save(lesson);
        return toResponse(lesson);
    }

    public LessonResponse reorderLesson(String courseId, String lessonId, String requesterId, Integer newOrder) {
        Course course = getCourseOrThrow(courseId);
        assertOwner(course, requesterId);

        Lesson lesson = getLessonOrThrow(lessonId, courseId);
        lesson.setOrder(newOrder);
        lesson.setUpdatedAt(Instant.now());

        lessonRepository.save(lesson);
        return toResponse(lesson);
    }

    private Course getCourseOrThrow(String courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    private Lesson getLessonOrThrow(String lessonId, String courseId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        if (!lesson.getCourseId().equals(courseId)) {
            throw new ResourceNotFoundException("Lesson does not belong to this course");
        }
        return lesson;
    }

    private void assertOwner(Course course, String requesterId) {
        if (!course.getCreatorId().equals(requesterId)) {
            throw new ForbiddenException("Only the course creator can modify its lessons");
        }
    }

    private LessonResponse toResponse(Lesson lesson) {
        return LessonResponse.builder()
                .id(lesson.getId())
                .courseId(lesson.getCourseId())
                .title(lesson.getTitle())
                .content(lesson.getContent())
                .order(lesson.getOrder())
                .videoUrl(lesson.getVideoUrl())
                .resourceUrl(lesson.getResourceUrl())
                .published(lesson.isPublished())
                .durationMinutes(lesson.getDurationMinutes())
                .createdAt(lesson.getCreatedAt())
                .updatedAt(lesson.getUpdatedAt())
                .build();
    }
}