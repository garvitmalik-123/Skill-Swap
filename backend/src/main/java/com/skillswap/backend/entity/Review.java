package com.skillswap.backend.entity;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reviews")
@CompoundIndexes({
        @CompoundIndex(name = "unique_user_course_review", def = "{'userId': 1, 'courseId': 1}", unique = true)
})
public class Review {

    @Id
    private String id;

    private String courseId;
    private String userId;       // reviewer

    private int rating;          // 1–5
    private String comment;

    private ReviewStatus status; // ACTIVE, REPORTED, REMOVED

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public enum ReviewStatus {
        ACTIVE, REPORTED, REMOVED
    }
}