package com.skillswap.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "course_progress")
@CompoundIndex(name = "user_lesson_idx", def = "{'userId': 1, 'lessonId': 1}", unique = true)
public class LessonProgress {

    @Id
    private String id;

    private String userId;

    private String courseId;

    private String lessonId;

    @Builder.Default
    private boolean completed = false;

    private Instant completedAt;
}