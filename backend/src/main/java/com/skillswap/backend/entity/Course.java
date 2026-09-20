package com.skillswap.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "courses")
public class Course {

    @Id
    private String id;

    @Indexed
    private String creatorId;

    private String title;
    private String description;

    @Indexed
    private String category;

    private List<String> skills;
    private Difficulty difficulty;
    private String language;
    private Integer durationMinutes;
    private List<String> learningObjectives;
    private List<String> prerequisites;
    private String thumbnailUrl;

    @Builder.Default
    private CourseType type = CourseType.FREE;

    private Double price;
    private Integer skillPointCost;

    @Builder.Default
    private CourseStatus status = CourseStatus.DRAFT;

    @Builder.Default
    private double averageRating = 0.0;

    @Builder.Default
    private int reviewCount = 0;

    @Builder.Default
    private int enrollmentCount = 0;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public enum CourseType {
        FREE, SKILLPOINT, PAID
    }

    public enum CourseStatus {
        DRAFT, PUBLISHED, ARCHIVED
    }

    public enum Difficulty {
        BEGINNER, INTERMEDIATE, ADVANCED
    }
}