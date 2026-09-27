package com.skillswap.backend.service;

import com.skillswap.backend.entity.Course;
import com.skillswap.backend.exception.BadRequestException;
import com.skillswap.backend.exception.DuplicateResourceException;
import com.skillswap.backend.repository.CourseEnrollmentRepository;
import com.skillswap.backend.repository.CourseRepository;
import com.skillswap.backend.repository.LessonProgressRepository;
import com.skillswap.backend.repository.LessonRepository;
import com.skillswap.backend.service.impl.EnrollmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceImplTest {

    @Mock
    private CourseEnrollmentRepository enrollmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private LessonProgressRepository lessonProgressRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    @Test
    void enroll_shouldThrowBadRequest_whenCourseIsNotPublished() {
        Course draftCourse = Course.builder()
                .id("course-1")
                .status(Course.CourseStatus.DRAFT)
                .type(Course.CourseType.FREE)
                .build();

        when(courseRepository.findById("course-1")).thenReturn(Optional.of(draftCourse));

        assertThatThrownBy(() -> enrollmentService.enroll("user-1", "course-1"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("published");
    }

    @Test
    void enroll_shouldThrowDuplicateResource_whenAlreadyEnrolled() {
        Course publishedCourse = Course.builder()
                .id("course-1")
                .status(Course.CourseStatus.PUBLISHED)
                .type(Course.CourseType.FREE)
                .build();

        when(courseRepository.findById("course-1")).thenReturn(Optional.of(publishedCourse));
        when(enrollmentRepository.existsByUserIdAndCourseId("user-1", "course-1")).thenReturn(true);

        assertThatThrownBy(() -> enrollmentService.enroll("user-1", "course-1"))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already enrolled");
    }

    @Test
    void enroll_shouldThrowBadRequest_whenCourseTypeIsSkillPoint() {
        Course skillPointCourse = Course.builder()
                .id("course-1")
                .status(Course.CourseStatus.PUBLISHED)
                .type(Course.CourseType.SKILLPOINT)
                .build();

        when(courseRepository.findById("course-1")).thenReturn(Optional.of(skillPointCourse));
        when(enrollmentRepository.existsByUserIdAndCourseId("user-1", "course-1")).thenReturn(false);

        assertThatThrownBy(() -> enrollmentService.enroll("user-1", "course-1"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("SkillPoint-based enrollment");
    }

    @Test
    void enroll_shouldThrowBadRequest_whenCourseTypeIsPaid() {
        Course paidCourse = Course.builder()
                .id("course-1")
                .status(Course.CourseStatus.PUBLISHED)
                .type(Course.CourseType.PAID)
                .build();

        when(courseRepository.findById("course-1")).thenReturn(Optional.of(paidCourse));
        when(enrollmentRepository.existsByUserIdAndCourseId("user-1", "course-1")).thenReturn(false);

        assertThatThrownBy(() -> enrollmentService.enroll("user-1", "course-1"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Paid course enrollment");
    }
}