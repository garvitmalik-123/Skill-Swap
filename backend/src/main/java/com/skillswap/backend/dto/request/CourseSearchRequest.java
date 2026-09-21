package com.skillswap.backend.dto.request;

import com.skillswap.backend.entity.Course;
import lombok.Data;

@Data
public class CourseSearchRequest {
    private String keyword;
    private String category;
    private String skill;
    private String creatorId;
    private Course.CourseType type;
    private Course.Difficulty difficulty;
    private String language;
    private Double minPrice;
    private Double maxPrice;
    private Double minRating;
    private Integer minDuration;
    private Integer maxDuration;
}