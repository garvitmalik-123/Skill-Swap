package com.skillswap.backend.service.impl;

import com.skillswap.backend.dto.request.CreateCourseRequest;
import com.skillswap.backend.dto.response.CourseResponse;
import com.skillswap.backend.entity.Course;
import com.skillswap.backend.exception.BadRequestException;
import com.skillswap.backend.exception.UnauthorizedException;
import com.skillswap.backend.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    private CreateCourseRequest createRequest;

    @BeforeEach
    void setUp() {
        createRequest = new CreateCourseRequest();
        createRequest.setTitle("Java Basics");
        createRequest.setDescription("Learn Java from scratch");
        createRequest.setCategory("Programming");
        createRequest.setDifficulty(Course.Difficulty.BEGINNER);
        createRequest.setType(Course.CourseType.FREE);
    }

    @Test
    void createCourse_shouldSaveWithDraftStatus() {
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> {
            Course c = invocation.getArgument(0);
            c.setId("course-1");
            return c;
        });

        CourseResponse response = courseService.createCourse("creator-1", createRequest);

        assertThat(response.getStatus()).isEqualTo(Course.CourseStatus.DRAFT);
        assertThat(response.getTitle()).isEqualTo("Java Basics");
    }

    @Test
    void createCourse_shouldThrowBadRequest_whenPaidCourseHasNoPrice() {
        createRequest.setType(Course.CourseType.PAID);
        createRequest.setPrice(null);

        assertThatThrownBy(() -> courseService.createCourse("creator-1", createRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("price");
    }

    @Test
    void updateCourse_shouldThrowUnauthorized_whenRequesterIsNotCreator() {
        Course existingCourse = Course.builder()
                .id("course-1")
                .creatorId("creator-1")
                .type(Course.CourseType.FREE)
                .status(Course.CourseStatus.DRAFT)
                .build();

        when(courseRepository.findById("course-1")).thenReturn(java.util.Optional.of(existingCourse));

        var updateRequest = new com.skillswap.backend.dto.request.UpdateCourseRequest();
        updateRequest.setTitle("Hacked Title");

        assertThatThrownBy(() -> courseService.updateCourse("course-1", "different-user", updateRequest))
                .isInstanceOf(UnauthorizedException.class);
    }
}