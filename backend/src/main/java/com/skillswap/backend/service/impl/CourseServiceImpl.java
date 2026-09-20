package com.skillswap.backend.service.impl;

import com.skillswap.backend.dto.request.CreateCourseRequest;
import com.skillswap.backend.dto.request.UpdateCourseRequest;
import com.skillswap.backend.dto.response.CourseResponse;
import com.skillswap.backend.entity.Course;
import com.skillswap.backend.exception.BadRequestException;
import com.skillswap.backend.exception.ResourceNotFoundException;
import com.skillswap.backend.exception.UnauthorizedException;
import com.skillswap.backend.repository.CourseRepository;
import com.skillswap.backend.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public CourseResponse createCourse(String creatorId, CreateCourseRequest request) {
        validatePricingByType(request.getType(), request.getPrice(), request.getSkillPointCost());

        Course course = Course.builder()
                .creatorId(creatorId)
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .skills(request.getSkills())
                .difficulty(request.getDifficulty())
                .language(request.getLanguage())
                .durationMinutes(request.getDurationMinutes())
                .learningObjectives(request.getLearningObjectives())
                .prerequisites(request.getPrerequisites())
                .thumbnailUrl(request.getThumbnailUrl())
                .type(request.getType())
                .price(request.getPrice())
                .skillPointCost(request.getSkillPointCost())
                .status(Course.CourseStatus.DRAFT)
                .build();

        Course saved = courseRepository.save(course);
        return toResponse(saved);
    }

    @Override
    public CourseResponse getCourseById(String courseId) {
        Course course = findCourseOrThrow(courseId);
        return toResponse(course);
    }

    @Override
    public Page<CourseResponse> getAllPublishedCourses(Pageable pageable) {
        return courseRepository.findByStatus(Course.CourseStatus.PUBLISHED, pageable)
                .map(this::toResponse);
    }

    @Override
    public CourseResponse updateCourse(String courseId, String requesterId, UpdateCourseRequest request) {
        Course course = findCourseOrThrow(courseId);
        verifyOwnership(course, requesterId);

        if (request.getTitle() != null) course.setTitle(request.getTitle());
        if (request.getDescription() != null) course.setDescription(request.getDescription());
        if (request.getCategory() != null) course.setCategory(request.getCategory());
        if (request.getSkills() != null) course.setSkills(request.getSkills());
        if (request.getDifficulty() != null) course.setDifficulty(request.getDifficulty());
        if (request.getLanguage() != null) course.setLanguage(request.getLanguage());
        if (request.getDurationMinutes() != null) course.setDurationMinutes(request.getDurationMinutes());
        if (request.getLearningObjectives() != null) course.setLearningObjectives(request.getLearningObjectives());
        if (request.getPrerequisites() != null) course.setPrerequisites(request.getPrerequisites());
        if (request.getThumbnailUrl() != null) course.setThumbnailUrl(request.getThumbnailUrl());
        if (request.getType() != null) course.setType(request.getType());
        if (request.getPrice() != null) course.setPrice(request.getPrice());
        if (request.getSkillPointCost() != null) course.setSkillPointCost(request.getSkillPointCost());

        validatePricingByType(course.getType(), course.getPrice(), course.getSkillPointCost());

        Course saved = courseRepository.save(course);
        return toResponse(saved);
    }

    @Override
    public void deleteCourse(String courseId, String requesterId) {
        Course course = findCourseOrThrow(courseId);
        verifyOwnership(course, requesterId);
        courseRepository.delete(course);
    }

    @Override
    public CourseResponse publishCourse(String courseId, String requesterId) {
        Course course = findCourseOrThrow(courseId);
        verifyOwnership(course, requesterId);

        if (course.getStatus() == Course.CourseStatus.ARCHIVED) {
            throw new BadRequestException("An archived course cannot be published directly");
        }

        course.setStatus(Course.CourseStatus.PUBLISHED);
        Course saved = courseRepository.save(course);
        return toResponse(saved);
    }

    @Override
    public CourseResponse archiveCourse(String courseId, String requesterId) {
        Course course = findCourseOrThrow(courseId);
        verifyOwnership(course, requesterId);

        course.setStatus(Course.CourseStatus.ARCHIVED);
        Course saved = courseRepository.save(course);
        return toResponse(saved);
    }

    private Course findCourseOrThrow(String courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
    }

    private void verifyOwnership(Course course, String requesterId) {
        if (!course.getCreatorId().equals(requesterId)) {
            throw new UnauthorizedException("Only the course creator can modify this course");
        }
    }

    private void validatePricingByType(Course.CourseType type, Double price, Integer skillPointCost) {
        if (type == Course.CourseType.PAID && (price == null || price <= 0)) {
            throw new BadRequestException("A positive price is required for PAID courses");
        }
        if (type == Course.CourseType.SKILLPOINT && (skillPointCost == null || skillPointCost <= 0)) {
            throw new BadRequestException("A positive SkillPoint cost is required for SKILLPOINT courses");
        }
    }

    private CourseResponse toResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .creatorId(course.getCreatorId())
                .title(course.getTitle())
                .description(course.getDescription())
                .category(course.getCategory())
                .skills(course.getSkills())
                .difficulty(course.getDifficulty())
                .language(course.getLanguage())
                .durationMinutes(course.getDurationMinutes())
                .learningObjectives(course.getLearningObjectives())
                .prerequisites(course.getPrerequisites())
                .thumbnailUrl(course.getThumbnailUrl())
                .type(course.getType())
                .price(course.getPrice())
                .skillPointCost(course.getSkillPointCost())
                .status(course.getStatus())
                .averageRating(course.getAverageRating())
                .reviewCount(course.getReviewCount())
                .enrollmentCount(course.getEnrollmentCount())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}