package com.skillswap.backend.dto.response;

import lombok.*;
import java.time.Instant;

@Data
@Builder
public class ReviewResponse {
    private String id;
    private String courseId;
    private String userId;
    private String userName;
    private int rating;
    private String comment;
    private Instant createdAt;
    private Instant updatedAt;
}