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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "course_lessons")
public class Lesson {

    @Id
    private String id;

    @Indexed
    private String courseId;

    private String title;
    private String content;

    private Integer order;

    private String videoUrl;
    private String resourceUrl;

    @Builder.Default
    private boolean published = false;

    private Integer durationMinutes;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}