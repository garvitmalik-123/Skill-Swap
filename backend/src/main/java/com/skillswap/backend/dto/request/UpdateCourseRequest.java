package com.skillswap.backend.dto.request;

import com.skillswap.backend.entity.Course;
import lombok.Data;

import java.util.List;

@Data
public class UpdateCourseRequest {

    private String title;
    private String description;
    private String category;
    private List<String> skills;
    private Course.Difficulty difficulty;
    private String language;
    private Integer durationMinutes;
    private List<String> learningObjectives;
    private List<String> prerequisites;
    private String thumbnailUrl;
    private Course.CourseType type;
    private Double price;
    private Integer skillPointCost;
}