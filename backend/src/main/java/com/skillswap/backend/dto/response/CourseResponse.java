package com.skillswap.backend.dto.response;

import com.skillswap.backend.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {

    private String id;
    private String creatorId;
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
    private Course.CourseStatus status;
    private double averageRating;
    private int reviewCount;
    private int enrollmentCount;
    private Instant createdAt;
    private Instant updatedAt;
}