package com.skillswap.backend.service;

import com.skillswap.backend.dto.request.CourseSearchRequest;
import com.skillswap.backend.dto.request.CreateCourseRequest;
import com.skillswap.backend.dto.request.UpdateCourseRequest;
import com.skillswap.backend.dto.response.CourseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {

    CourseResponse createCourse(String creatorId, CreateCourseRequest request);

    CourseResponse getCourseById(String courseId);

    Page<CourseResponse> getAllPublishedCourses(Pageable pageable);

    Page<CourseResponse> searchCourses(CourseSearchRequest filters, Pageable pageable);

    CourseResponse updateCourse(String courseId, String requesterId, UpdateCourseRequest request);

    void deleteCourse(String courseId, String requesterId);

    CourseResponse publishCourse(String courseId, String requesterId);

    CourseResponse archiveCourse(String courseId, String requesterId);
}
