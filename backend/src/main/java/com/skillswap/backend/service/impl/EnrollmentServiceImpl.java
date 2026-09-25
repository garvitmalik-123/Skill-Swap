package com.skillswap.backend.service.impl;

import com.skillswap.backend.dto.response.EnrollmentResponse;
import com.skillswap.backend.dto.response.ProgressResponse;
import com.skillswap.backend.entity.Course;
import com.skillswap.backend.entity.CourseEnrollment;
import com.skillswap.backend.entity.Lesson;
import com.skillswap.backend.entity.LessonProgress;
import com.skillswap.backend.entity.Notification.NotificationType;
import com.skillswap.backend.exception.BadRequestException;
import com.skillswap.backend.exception.DuplicateResourceException;
import com.skillswap.backend.exception.ResourceNotFoundException;
import com.skillswap.backend.repository.CourseEnrollmentRepository;
import com.skillswap.backend.repository.CourseRepository;
import com.skillswap.backend.repository.LessonProgressRepository;
import com.skillswap.backend.repository.LessonRepository;
import com.skillswap.backend.service.EnrollmentService;
import com.skillswap.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final CourseEnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final NotificationService notificationService;

    @Override
    public EnrollmentResponse enroll(String userId, String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        if (course.getStatus() != Course.CourseStatus.PUBLISHED) {
            throw new BadRequestException("Only published courses can be enrolled in");
        }

        if (enrollmentRepository.existsByUserIdAndCourseId(userId, courseId)) {
            throw new DuplicateResourceException("You are already enrolled in this course");
        }

        if (course.getType() == Course.CourseType.SKILLPOINT) {
            throw new BadRequestException("SkillPoint-based enrollment is not yet supported. This feature is coming soon.");
        }
        if (course.getType() == Course.CourseType.PAID) {
            throw new BadRequestException("Paid course enrollment is not yet supported. This feature is coming soon.");
        }

        CourseEnrollment enrollment = CourseEnrollment.builder()
                .userId(userId)
                .courseId(courseId)
                .enrollmentType(course.getType())
                .status(CourseEnrollment.EnrollmentStatus.ACTIVE)
                .enrolledAt(Instant.now())
                .build();

        CourseEnrollment saved = enrollmentRepository.save(enrollment);

        course.setEnrollmentCount(course.getEnrollmentCount() + 1);
        courseRepository.save(course);

        notificationService.notify(
                userId,
                NotificationType.COURSE_ENROLLMENT,
                "Enrolled successfully",
                "You enrolled in \"" + course.getTitle() + "\"",
                course.getId(),
                "COURSE");

        notificationService.notify(
                course.getCreatorId(),
                NotificationType.NEW_LEARNER_ENROLLED,
                "New learner enrolled",
                "Someone enrolled in your course \"" + course.getTitle() + "\"",
                course.getId(),
                "COURSE");

        return toResponse(saved, course.getTitle());
    }

    @Override
    public Page<EnrollmentResponse> getMyEnrollments(String userId, Pageable pageable) {
        return enrollmentRepository.findByUserId(userId, pageable)
                .map(enrollment -> {
                    String title = courseRepository.findById(enrollment.getCourseId())
                            .map(Course::getTitle)
                            .orElse("Unknown course");
                    return toResponse(enrollment, title);
                });
    }

    @Override
    public EnrollmentResponse getEnrollment(String userId, String courseId) {
        CourseEnrollment enrollment = findEnrollmentOrThrow(userId, courseId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        return toResponse(enrollment, course.getTitle());
    }

    @Override
    public void markLessonComplete(String userId, String courseId, String lessonId) {
        findEnrollmentOrThrow(userId, courseId);

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        if (!lesson.getCourseId().equals(courseId)) {
            throw new ResourceNotFoundException("Lesson does not belong to this course");
        }

        LessonProgress progress = lessonProgressRepository.findByUserIdAndLessonId(userId, lessonId)
                .orElse(LessonProgress.builder()
                        .userId(userId)
                        .courseId(courseId)
                        .lessonId(lessonId)
                        .build());

        progress.setCompleted(true);
        progress.setCompletedAt(Instant.now());
        lessonProgressRepository.save(progress);

        checkAndMarkCourseCompletion(userId, courseId);
    }

    @Override
    public ProgressResponse getProgress(String userId, String courseId) {
        findEnrollmentOrThrow(userId, courseId);

        List<Lesson> publishedLessons = lessonRepository.findByCourseIdAndPublishedTrueOrderByOrderAsc(courseId);
        long completedCount = lessonProgressRepository.countByUserIdAndCourseIdAndCompletedTrue(userId, courseId);

        int total = publishedLessons.size();
        double percentage = total == 0 ? 0.0 : (completedCount * 100.0) / total;

        return ProgressResponse.builder()
                .courseId(courseId)
                .totalLessons(total)
                .completedLessons((int) completedCount)
                .progressPercentage(Math.round(percentage * 100.0) / 100.0)
                .courseCompleted(total > 0 && completedCount >= total)
                .build();
    }

    private void checkAndMarkCourseCompletion(String userId, String courseId) {
        List<Lesson> publishedLessons = lessonRepository.findByCourseIdAndPublishedTrueOrderByOrderAsc(courseId);
        long completedCount = lessonProgressRepository.countByUserIdAndCourseIdAndCompletedTrue(userId, courseId);

        if (!publishedLessons.isEmpty() && completedCount >= publishedLessons.size()) {
            enrollmentRepository.findByUserIdAndCourseId(userId, courseId).ifPresent(enrollment -> {
                if (enrollment.getStatus() != CourseEnrollment.EnrollmentStatus.COMPLETED) {
                    enrollment.setStatus(CourseEnrollment.EnrollmentStatus.COMPLETED);
                    enrollment.setCompletedAt(Instant.now());
                    enrollmentRepository.save(enrollment);

                    courseRepository.findById(courseId).ifPresent(course ->
                            notificationService.notify(
                                    userId,
                                    NotificationType.COURSE_COMPLETION,
                                    "Course completed!",
                                    "You completed \"" + course.getTitle() + "\"",
                                    courseId,
                                    "COURSE"));
                }
            });
        }
    }

    private CourseEnrollment findEnrollmentOrThrow(String userId, String courseId) {
        return enrollmentRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("You are not enrolled in this course"));
    }

    private EnrollmentResponse toResponse(CourseEnrollment enrollment, String courseTitle) {
        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .courseId(enrollment.getCourseId())
                .courseTitle(courseTitle)
                .enrollmentType(enrollment.getEnrollmentType())
                .status(enrollment.getStatus())
                .enrolledAt(enrollment.getEnrolledAt())
                .completedAt(enrollment.getCompletedAt())
                .build();
    }
}