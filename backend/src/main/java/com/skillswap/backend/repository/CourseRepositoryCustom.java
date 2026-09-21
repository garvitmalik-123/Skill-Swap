package com.skillswap.backend.repository;

import com.skillswap.backend.dto.request.CourseSearchRequest;
import com.skillswap.backend.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseRepositoryCustom {
    Page<Course> search(CourseSearchRequest filters, Pageable pageable);
}