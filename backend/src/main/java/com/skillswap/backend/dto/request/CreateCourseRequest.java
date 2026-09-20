package com.skillswap.backend.dto.request;

import com.skillswap.backend.entity.Course;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class CreateCourseRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Category is required")
    private String category;

    private List<String> skills;

    @NotNull(message = "Difficulty is required")
    private Course.Difficulty difficulty;

    private String language;

    @Positive(message = "Duration must be positive")
    private Integer durationMinutes;

    private List<String> learningObjectives;
    private List<String> prerequisites;
    private String thumbnailUrl;

    @NotNull(message = "Course type is required")
    private Course.CourseType type;

    private Double price;
    private Integer skillPointCost;
}